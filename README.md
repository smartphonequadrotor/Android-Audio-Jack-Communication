# Communication over the Audio Jack for Android phones

This repository consists of various ways that Android phones could communicate with external devices using the audio jack. Some of the possibilities are

* Creating pulses to emulate digital signals. One of the output channels could be used as a clock and the other can be used to send data out. The microphone can be used to get data from the device onto the phone. This way, it may be possible to emulate the SPI protocol.
* Using DTMF tones to send signals out from the phone and using FFT analysis on the recorded sound to receive data from the external device.

