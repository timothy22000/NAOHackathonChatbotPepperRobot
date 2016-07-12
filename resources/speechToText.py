#!/usr/bin/python3


import speech_recognition as sr
# obtain path to "english.wav" in the same folder as this script
from os import path
from paramiko import SSHClient
from scp import SCPClient
import time

ssh = SSHClient()
ssh.load_system_host_keys()
server = "138.37.60.38"
port = 22
user = "nao"
password = "nao"
ssh.connect(server, port, user, password)

# SCPCLient takes a paramiko transport as its only argument
scp = SCPClient(ssh.get_transport())
audio_file = "/Users/lavanya/social-hackathon/pepper/resources/audio.ogg"


scp.get("~/recordings/microphones/audio.ogg", audio_file)

AUDIO_FILE = path.join(path.dirname(path.realpath(__file__)), audio_file)
# use the audio file as the audio source
r = sr.Recognizer()

with sr.AudioFile(AUDIO_FILE) as source:
    audio = r.record(source)# read the entire audio file


# recognize speech using Google Speech Recognition
try:
    # for testing purposes, we're just using the default API key
    # to use another API key, use `r.recognize_google(audio, key="GOOGLE_SPEECH_RECOGNITION_API_KEY")`
    # instead of `r.recognize_google(audio)`
    print (r.recognize_google(audio))
except sr.UnknownValueError:
    print("Google Speech Recognition could not understand audio")
except sr.RequestError as e:
    print("Could not request results from Google Speech Recognition service; {0}".format(e))

