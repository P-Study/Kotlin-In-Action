package com.pteam.kotlin.hyuk.chapter05.code;

public class JavaFunction {
	static void postponeComputation(int delay, Runnable computation) {
		try {
			Thread.sleep(delay);
			computation.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
