######################################################################################################
# MFCC là một tập hợp các hệ số mô tả hình dạng phổ công suất của tín hiệu âm thanh.                 #
# Chuyển đổi tín hiệu âm thanh thô --> tần số bằng cách sử dụng Biến đổi Fourier rời rạc (DFT)       #
# Áp dụng thang đo mel để ước tính nhận thức thính giác của con người về tần số âm thanh.            #
# Cuối cùng, các hệ số epstral được tính toán từ phổ thang đo mel.                                   #
######################################################################################################
import librosa
import IPython.display as ipd
import matplotlib.pyplot as plt
import librosa.display
from sklearn.preprocessing import minmax_scale
import numpy as np
import os
from sklearn.model_selection import train_test_split
import re
import pandas as pd
import audio_utils
import scipy.fft as fft
import joblib
audio_utils_instance = audio_utils.AudioUtils()


folder_path = 'C:\\Users\\ThuyLe\\Desktop\\abcd\\data\\Mix'

RATE = 44100
FRAME = 512

def calculate_rms_energy(frame):
    return np.sqrt(np.mean(np.square(frame)))

def feature_extract(file):
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
    rms_energy = [calculate_rms_energy(data[i:i + FRAME]) for i in range(0, len(data), int(FRAME / 3))]
    feature_rms_energy = np.mean(rms_energy)

    features = np.hstack([mfcc_scaled,feature_zcr,feature_ste,feature_steacc,feature_stezcr,
                         feature_spectral_centroid,feature_spectral_bandwidth,
                         feature_spectral_rolloff,feature_spectral_flatness, feature_rms_energy])

    return features

features_all = []
labels = []


for filename in os.listdir(folder_path):
    if os.path.isfile(os.path.join(folder_path, filename)):
        file_path = os.path.join(folder_path, filename)
        if re.search(r'cry', filename, re.IGNORECASE):
            label = 1  # Gán nhãn 1 nếu tên tệp chứa "cry"
        else:
            label = 0  # Gán nhãn 0 cho các tệp khác
        features = feature_extract(file_path)
        features_all.append(features) 
        labels.append(label)

# Tạo DataFrame từ dữ liệu
data = {'Features': features_all, 'Label': labels}
df = pd.DataFrame(data)

# Lưu DataFrame vào file CSV
df.to_csv('dataset_save_file.csv', sep=",", index=False, encoding="utf8")

X = np.array(df['Features'].tolist())
Y = np.array(df['Label'].tolist())

X_train, X_test, Y_train, Y_test = train_test_split(X, Y,test_size=0.2, random_state=0)
#####-----------KNN------------------------------------
from sklearn.neighbors import KNeighborsClassifier

# Tạo một mô hình KNN
knn_model = KNeighborsClassifier(n_neighbors=15)  # Số lân cận (k) có thể thay đổi

# Huấn luyện mô hình trên tập huấn luyện
knn_model.fit(X_train, Y_train)
knn_accuracy = knn_model.score(X_test, Y_test)
print(f"KNN Accuracy: {knn_accuracy}")

# Lưu mô hình vào file .pkl
joblib.dump(knn_model, 'knn_model.pkl')
