package net.hypixel.api.util;

@SuppressWarnings("serial")
public class APIThrottleException extends HypixelAPIException {
    public APIThrottleException() {
        super("You have passed the API throttle limit!");
    }
}
