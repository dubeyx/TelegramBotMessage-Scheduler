package com.courpedia.crackjeepro.Services;


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class ProcessMessage {

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static Map<String,Long> channelList = new HashMap<String,Long>();
    public static void parseCommand(Long chatId, String msg){

        Bot newbot= new Bot();;

        if(msg.equalsIgnoreCase("/start")){

            newbot.sendText(chatId,"Welcome to IIT JEE Quiz Bot , How can I help you !!");


        }



        if(msg.equalsIgnoreCase("/allAdminChannel")){

            newbot.sendText(chatId,channelList.toString());


        }

        if(msg.equalsIgnoreCase("/stopQuiz")){

            if(!S3ImageProcessor.flag) {
                S3ImageProcessor.flag = true;
                newbot.sendText(chatId,"Stopped !!");

            }else{
                newbot.sendText(chatId,"Already Stopped");
            }






        }

        if(msg.equalsIgnoreCase("/help")){

            String  s ="/start  : to Start the JeeQuiz Bot \n " +
                    "/help : to get All List of Commands \n" +
                    "/allAdminChannel : to get All Channel subscribed \n" +
                    "/startQuiz : to start quiz on these channels \n" +
                    "/stopQuiz : to stop quiz on these channels";

            newbot.sendText(chatId,s);


        }


        if(msg.equalsIgnoreCase("/startQuiz")){


            if(ProcessMessage.channelList.size()>0) {

                newbot.sendText(chatId,"Quiz Started");
                executorService.execute(new S3ImageProcessor());}
            else{
                newbot.sendText(chatId,"No Channel Is subscribed !!");
            }

        }

    }




}



