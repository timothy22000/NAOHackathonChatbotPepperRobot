package com.thinktankcrew.social.hackathon;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALSoundDetection;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import com.aldebaran.qi.helper.proxies.ALAudioDevice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktankcrew.social.hackathon.model.MitsukuResponse;
import sun.plugin2.util.SystemUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.IOException;
import java.util.List;

public class PepperApplication {
    private static ALTextToSpeech alTextToSpeech;
    private static ALMemory memory;
    private static ALTextToSpeech tts;
    private static String file_name;
    private static String speechToTextPythonScriptPath;
    private static String pandoraBotScriptPath;
    private static ALAudioDevice audioDevice;
    private static long time;
    private static boolean processing = false;
    private static String yourpath= "/timothysum/dev/";

	public static void main(String[] args) {
        //Pepper robot URL
        String robotUrl = "tcp://138.37.60.38:9559";

        Application application = new Application(args, robotUrl);

//		if (args.length < 3) {
//			System.err.println("Usage: PepperApplication <recordingFileName> <speechToTextScript> <pandoraBotScript>");
//            /*
//             recordingFileName = /home/nao/recordings/microphones/audio.ogg
//             speechToTextScript = /Users/timothy/dev/pepper/resources/speechToText.py
//             pandoraBotScript = /Users/timothy/dev/pepper/resources/pandoraBot.py
//            */
//			System.exit(1);
//		}

		 file_name = "/home/nao/recordings/microphones/audio.ogg";
		 speechToTextPythonScriptPath = "/Users" + yourpath + "pepper/resources/speechToText.py";
		 pandoraBotScriptPath = "/Users" + yourpath + "pepper/resources/pandoraBot.py";

		try {
            application.start();
            alTextToSpeech = new ALTextToSpeech(application.session());
            audioDevice = new ALAudioDevice(application.session());
            memory = new ALMemory(application.session());
            audioDevice.stopMicrophonesRecording();

            ALSoundDetection alSoundDetection = new ALSoundDetection(application.session());

            alSoundDetection.setParameter("Sensibility", 0.7f);
            run();
            application.run();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

	private static String stringMessage(Process speechToTextProcess) throws IOException {
		BufferedReader stdInput = new BufferedReader(new
				InputStreamReader(speechToTextProcess.getInputStream()));

        String aString;
		StringBuilder stringBuilder = new StringBuilder();
		while ((aString = stdInput.readLine()) != null) {
			stringBuilder.append(aString);

		}
		aString = stringBuilder.toString();

		return aString;
	}

    private static String jsonMessage(Process speechToTextProcess) throws IOException {
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(speechToTextProcess.getInputStream()));

        String aString;
        StringBuilder stringBuilder = new StringBuilder();
        while ((aString = stdInput.readLine()) != null) {
            stringBuilder.append(aString);

        }
        aString = stringBuilder.toString();

        //System.out.println(aString);

        return aString;
    }


    public static String errorMessage(InputStream inputStream) throws IOException {
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(inputStream));

        String aString;
        StringBuilder stringBuilder = new StringBuilder();
        while ((aString = stdInput.readLine()) != null) {
            stringBuilder.append(aString);

        }
        aString = stringBuilder.toString();

        //System.out.println(aString);

        return aString;
    }

    private static void startRecording() throws InterruptedException, CallError {

        audioDevice.startMicrophonesRecording(file_name);
    }


    private static void stopRecording() throws InterruptedException, CallError, IOException {
        Boolean stop = processing;

        while (stop) {
            long currentTime = System.currentTimeMillis();
            if(currentTime - time > 3000){
                System.out.println("I STOPPED RECORDING");
                audioDevice.stopMicrophonesRecording();
                stop = false;
                processAudio();
                System.out.println("STOP");

            }

            Thread.sleep(10);

        }

    }

    public static void processAudio() throws IOException, InterruptedException, CallError {

        String speechToTextCommand = "python3 " + speechToTextPythonScriptPath;

        Process speechToTextProcess = Runtime.getRuntime().exec(speechToTextCommand);

        System.out.println(errorMessage(speechToTextProcess.getErrorStream()));

        String speechToTextString = stringMessage(speechToTextProcess);

        if(speechToTextString == null || speechToTextString.trim().isEmpty() ||
                speechToTextString.startsWith("Google Speech Recognition could not understand audio")) {

            alTextToSpeech.say("Sorry, I couldn't understand that");


        } else {


            System.out.println("Google " + speechToTextString);

            String command = "/Users/"
                    + yourpath
                    + "/pepper/resources/mitsukuBot.sh "
                    + speechToTextString.replace(" ", "+");

            Process pandoraBotProcess = Runtime.getRuntime().exec(command);

            String pandoraBotResponse = jsonMessage(pandoraBotProcess);

            ObjectMapper objectMapper = new ObjectMapper();

            MitsukuResponse mitsukuResponse = objectMapper.readValue(
                    pandoraBotResponse,
                    MitsukuResponse.class
            );

            //Mitsuku weird response cleaning.
            if(!mitsukuResponse.getResponses().isEmpty()) {
                pandoraBotResponse = mitsukuResponse.getResponses().get(0)
                        .replace("\\t", "")
                        .replace("\\n", "")
                        .replace("\\r\\n", "")
                        .replace("\\S", "")
                        .replace("[^a-zA-Z ]", "")
                        .replace("(DEMO|demo)", "")
                        .replace(
                                "Powered by <a href=\"http://www.pandorabots.com\">Pandorabots</a>",
                                ""
                        );

                System.out.println(pandoraBotResponse);

                alTextToSpeech.say(pandoraBotResponse);
            } else {
                alTextToSpeech.say("I am sorry ");
            }

        }

        processing = false;

    }

    public static void run() throws Exception {

        memory.subscribeToEvent(
                "SoundDetected", arg0 -> {
                    time = System.currentTimeMillis();
                    if(!processing) {
                        processing = true;
                        startRecording();
                        System.out.println("START");

                        try {
                            stopRecording();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

}
