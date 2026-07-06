package com.github.mcreeper12731;

import com.github.mcreeper12731.application.HeadlessApplication;
import com.github.mcreeper12731.utility.Log;

public class Main {
    public static void main(String[] args) {

        HeadlessApplication application = new HeadlessApplication();

        application.run();

        Log.debug("Main", application.getGame().getWinner());
    }
}