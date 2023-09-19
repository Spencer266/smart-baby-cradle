CLASSIFY BABY CRY AND NOISE 
1. Setup: 
    - Sử dụng python 3.11
    - pip install các thư viện: librosa, numpy, os, re, pandas, scipy, 
      joblib, superml, audio-utils, scikit-mlm
2. Chạy code:
    - Vào folder main có 2 code "model_training.py" và "predict_cry.py"
    - Chạy "model_training.py" trước, để nó tự tạo file "data_save.csv và knn_model.plk". Quá trình này mất tầm 3-5 phút
    (lưu ý nhớ đổi tên file folder_path sao cho phù hợp với vị trí data trên máy tính của bạn). Lưu ý file này chỉ cần chạy lại 1 lần duy nhất để lấy model thôi.
    - Sau đó chạy "predict_cry.py" để detect âm thanh đầu vào. Âm thanh đầu vào sẽ là âm thanh thu được từ micro I2S (được đẩy lên pi qua gateway)

