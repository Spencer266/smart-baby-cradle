import librosa
import IPython.display as ipd
import matplotlib.pyplot as plt
import librosa.display
from sklearn.preprocessing import minmax_scale
import numpy as np
from sklearn.model_selection import train_test_split
import pandas as pd
import scipy.fft as fft
import joblib
import audio_utils


RATE = 44100
FRAME = 512
FILE_PATH = 'data/FTP/files/record.wav'



class Predictor:
    def __init__(self):
        self.loaded_model = joblib.load('scripts/ML/knn_model.pkl')

    def calculate_rms_energy(self, frame):
        return np.sqrt(np.mean(np.square(frame)))

    def feature_extract(self, file):
        features = np.array([])

        data,sample_rate=librosa.load(file, sr=RATE)
        mfcc = librosa.feature.mfcc(y = data, sr = RATE, n_mfcc = 13)
        mfcc_scaled = np.mean(mfcc.T, axis = 0)

        zcr = librosa.feature.zero_crossing_rate(data, frame_length=FRAME, hop_length=int(FRAME/3))
        feature_zcr = np.mean(zcr)

        ste = audio_utils.AudioUtils.ste(data, 'hamming', int(20 * 0.001 * RATE))
        feature_ste = np.mean(ste) 

        ste_acc = np.diff(ste)
        feature_steacc = np.mean(ste_acc[ste_acc > 0])

        stzcr = audio_utils.AudioUtils.stzcr(data, 'hamming', int(20 * 0.001 * RATE))
        feature_stezcr = np.mean(stzcr)

        spectral_centroid = librosa.feature.spectral_centroid(y=data, sr=RATE, hop_length=int(FRAME/3))

        feature_spectral_centroid = np.mean(spectral_centroid)

        spectral_bandwidth = librosa.feature.spectral_bandwidth(y=data, sr=RATE, hop_length= int(FRAME/3))
        feature_spectral_bandwidth = np.mean(spectral_bandwidth)

        spectral_rolloff = librosa.feature.spectral_rolloff(y=data, sr=RATE, hop_length= int(FRAME/3),
                                                roll_percent=0.90)
        feature_spectral_rolloff = np.mean(spectral_rolloff)

        spectral_flatness = librosa.feature.spectral_flatness(y=data, hop_length=int(FRAME/3))
        feature_spectral_flatness = np.mean(spectral_flatness)


        # Tính toán và thêm đặc trưng RMS Energy
        rms_energy = [self.calculate_rms_energy(data[i:i + FRAME]) for i in range(0, len(data), int(FRAME / 3))]
        feature_rms_energy = np.mean(rms_energy)

        features = np.hstack([mfcc_scaled,feature_zcr,feature_ste,feature_steacc,feature_stezcr,
                            feature_spectral_centroid,feature_spectral_bandwidth,
                            feature_spectral_rolloff,feature_spectral_flatness, feature_rms_energy])

        return features
    
    def predict(self):
        feature_test = self.feature_extract(FILE_PATH)

        # Dự đoán trên dữ liệu mới
        new_data = feature_test 
        prediction = self.loaded_model.predict([new_data])
        
        return prediction[0]
