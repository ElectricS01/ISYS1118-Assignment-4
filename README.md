# ISYS1118-Assignment-4
ISYS1118: Assignment 4

## Running the application
```shell
mvn clean compile exec:java
```

## Running the tests
```shell
mvn clean test
```

## Generate Gource video (macOS)
```shell
gource -1280x720 -o - | ffmpeg -y -r 60 -f image2pipe -vcodec ppm -i - -vcodec h264_videotoolbox -preset slower -pix_fmt yuv420p -crf 1 -threads 0 -bf 0 gource.mp4
```

## Generate Gource video (Windows)
```shell
gource -1280x720 -o - | ffmpeg -y -r 60 -f image2pipe -vcodec ppm -i - -vcodec libvpx -b 10000K gource.webm
```