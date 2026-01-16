package com.lavacro.songlist;

public class SongListException extends Exception {
	private static final long serialVersionUID = -1;

	public SongListException(String message, Throwable error) {
		super(message, error);
	}

	public SongListException(String message) {
		super(message);
	}
}
