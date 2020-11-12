package Mentalist.utils;

import java.util.LinkedList;

public class Event {
    private EventClass type;

    private String message;

    private Offer offer;

    public enum EventClass {
        SEND_EXPRESSION, OFFER_IN_PROGRESS, SEND_OFFER, SEND_MESSAGE, FORMAL_ACCEPT, GAME_START, GAME_END, TIME, FORMAL_QUIT;
    }

    public enum SubClass {
        FAVOR_REQUEST, FAVOR_ACCEPT, FAVOR_REJECT, FAVOR_RETURN, PREF_REQUEST, PREF_INFO, PREF_SPECIFIC_REQUEST, PREF_WITHHOLD, BATNA_REQUEST, BATNA_INFO, OFFER_REQUEST_POS, OFFER_REQUEST_NEG, OFFER_PROPOSE, THREAT_POS, THREAT_NEG, CONFUSION, OFFER_ACCEPT, OFFER_REJECT, GENERIC_POS, GENERIC_NEG, TIMING, NONE;
    }

    private int delay = 0;

    private int owner = -2;

    private Preference preference;

    private int code = -1;

    private int duration = 2000;

    private int value = -1;

    private boolean priority = false;

    private boolean flushable = true;

    private SubClass sc = SubClass.NONE;

    @Deprecated
    public Event(int owner, EventClass eventClass, Offer offer) {
        this.type = eventClass;
        if (eventClass != EventClass.SEND_OFFER)
            throw new UnsupportedOperationException("You have tried to create an event formatted as an Offer, but isn't an offer.");
        this.offer = offer;
        this.owner = owner;
        this.priority = true;
    }

    public Event(int owner, EventClass eventClass, int delay) {
        this.type = eventClass;
        if (eventClass != EventClass.FORMAL_ACCEPT && eventClass != EventClass.OFFER_IN_PROGRESS && eventClass != EventClass.FORMAL_QUIT)
            throw new UnsupportedOperationException("You have tried to create an event formatted as a formal acceptance/quit or offer-in-progress, but isn't that.");
        this.delay = delay;
        this.owner = owner;
        if (eventClass != EventClass.OFFER_IN_PROGRESS) {
            this.flushable = false;
            this.priority = true;
        }
    }

    public Event(int owner, EventClass eventClass, String emotion, int duration, int delay) {
        LinkedList<String> supportedEmo = new LinkedList<>();
        supportedEmo.add("angry");
        supportedEmo.add("happy");
        supportedEmo.add("surprised");
        supportedEmo.add("disgusted");
        supportedEmo.add("afraid");
        supportedEmo.add("neutral");
        supportedEmo.add("sad");
        supportedEmo.add("insincereSmile");
        if (!supportedEmo.contains(emotion))
            throw new UnsupportedOperationException("You have specified an emotion that is not supported! Currently supported are: angry, happy, surprised, disgusted, afraid, neutral, sad, insincereSmile");
        this.type = eventClass;
        if (eventClass != EventClass.SEND_EXPRESSION)
            throw new UnsupportedOperationException("You have tried to create an event formatted as an Expression, but isn't an Expression.");
        this.delay = delay;
        this.owner = owner;
        this.duration = duration;
        this.message = emotion;
    }

    public Event(int owner, EventClass eventClass, String message, int delay) {
        this.type = eventClass;
        if (this.type != EventClass.SEND_MESSAGE)
            throw new UnsupportedOperationException("You have tried to encode a message for event formatted as a Message, but isn't a message.");
        this.message = message;
        this.delay = delay;
        this.owner = owner;
    }

    public Event(int owner, EventClass eventClass, int value, String message, int delay) {
        this.type = eventClass;
        if (this.type != EventClass.SEND_MESSAGE)
            throw new UnsupportedOperationException("You have tried to encode a message for event formatted as a Message, but isn't a message.");
        this.message = message;
        this.delay = delay;
        this.owner = owner;
        this.value = value;
        this.flushable = false;
    }

    public Event(int owner, EventClass eventClass, SubClass sc, String message, int delay) {
        this.type = eventClass;
        if (this.type != EventClass.SEND_MESSAGE)
            throw new UnsupportedOperationException("You have tried to encode a message for event formatted as a Message, but isn't a message.");
        this.message = message;
        this.delay = delay;
        this.owner = owner;
        this.flushable = false;
        this.sc = sc;
    }

    public Event(int owner, EventClass eventClass, SubClass sc, int value, String message, int delay) {
        this.type = eventClass;
        if (this.type != EventClass.SEND_MESSAGE)
            throw new UnsupportedOperationException("You have tried to encode a message for event formatted as a Message, but isn't a message.");
        this.message = message;
        this.delay = delay;
        this.owner = owner;
        this.value = value;
        this.flushable = false;
        this.sc = sc;
    }

    protected Event(int owner, EventClass eventClass) {
        this.type = eventClass;
        if (eventClass != EventClass.GAME_START && eventClass != EventClass.GAME_END)
            throw new UnsupportedOperationException("You have tried to create an event formatted as a GAME event, but isn't that.");
        this.owner = -1;
        this.priority = true;
        this.flushable = false;
    }

    protected Event(int owner, EventClass eventClass, String time) {
        this.type = eventClass;
        if (this.type != EventClass.TIME)
            throw new UnsupportedOperationException("You have tried to encode a message for event formatted as TIME, but isn't a a TIME event.");
        this.message = time;
        this.delay = 0;
        this.owner = -1;
        this.flushable = false;
    }

    public Event(int owner, EventClass eventClass, Offer offer, int delay) {
        this.type = eventClass;
        if (eventClass != EventClass.SEND_OFFER)
            throw new UnsupportedOperationException("You have tried to create an event formatted as an Offer, but isn't an offer.");
        this.offer = offer;
        this.delay = delay;
        this.owner = owner;
        this.priority = true;
    }

    protected Event(int owner, EventClass eventClass, Offer offer, String message, int delay) {
        this.type = eventClass;
        if (eventClass != EventClass.SEND_OFFER)
            throw new UnsupportedOperationException("You have tried to create an event formatted as an Offer, but isn't an offer.");
        this.offer = offer;
        this.delay = delay;
        this.message = message;
        this.owner = owner;
        this.priority = true;
    }

    public void encodePreferenceData(Preference preference) {
        if (this.type != EventClass.SEND_MESSAGE)
            throw new UnsupportedOperationException("You have tried to encode a message for event formatted as a Message, but isn't a message.");
        this.preference = preference;
        this.priority = true;
    }

    public void encodeMessageCode(int code) {
        if (this.type != EventClass.SEND_MESSAGE)
            throw new UnsupportedOperationException("You have tried to encode a message for event formatted as an Message, but isn't an message.");
        this.code = code;
        switch (this.code) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 26:
                this.sc = SubClass.GENERIC_POS;
                break;
            case 11:
            case 12:
            case 13:
            case 14:
            case 27:
                this.sc = SubClass.GENERIC_NEG;
                break;
            case 22:
                this.sc = SubClass.FAVOR_REQUEST;
                break;
            case 103:
                this.sc = SubClass.FAVOR_ACCEPT;
                break;
            case 102:
                this.sc = SubClass.FAVOR_REJECT;
                break;
            case 24:
                this.sc = SubClass.FAVOR_RETURN;
                break;
            case 31:
                this.sc = SubClass.PREF_REQUEST;
                break;
            case 51:
                this.sc = SubClass.PREF_INFO;
                break;
            case 50:
                this.sc = SubClass.PREF_SPECIFIC_REQUEST;
                break;
            case 500:
                this.sc = SubClass.PREF_WITHHOLD;
                break;
            case 21:
                this.sc = SubClass.BATNA_REQUEST;
                break;
            case 200:
                this.sc = SubClass.BATNA_INFO;
                break;
            case 5:
                this.sc = SubClass.OFFER_REQUEST_POS;
                break;
            case 15:
                this.sc = SubClass.OFFER_REQUEST_NEG;
                break;
            case 6:
                this.sc = SubClass.THREAT_POS;
                break;
            case 16:
                this.sc = SubClass.THREAT_NEG;
                break;
            case 30:
                this.sc = SubClass.CONFUSION;
                break;
            case 101:
                this.sc = SubClass.OFFER_ACCEPT;
                break;
            case 100:
                this.sc = SubClass.OFFER_REJECT;
                break;
            case 52:
                this.sc = SubClass.TIMING;
                break;
            default:
                this.sc = SubClass.NONE;
                break;
        }
        ServletUtils.log("Encoded message with code: " + code + " and SubClass: " + this.sc, ServletUtils.DebugLevels.DEBUG);
    }

    public EventClass getType() {
        return this.type;
    }

    public SubClass getSubClass() {
        return this.sc;
    }

    public String getMessage() {
        return this.message;
    }

    public Offer getOffer() {
        return this.offer;
    }

    public int getDelay() {
        return this.delay;
    }

    public int getOwner() {
        return this.owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public Preference getPreference() {
        return this.preference;
    }

    public int getMessageCode() {
        return this.code;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isPriority() {
        return this.priority;
    }

    public void setPriority(boolean prio) {
        this.priority = prio;
    }

    public boolean isFlushable() {
        return this.flushable;
    }

    public void setFlushable(boolean flushable) {
        this.flushable = flushable;
    }

    public boolean equals(Object ePrime) {
        boolean equalType, equalOwner, equalMessage, equalOffer, equalPref, equalValue;
        Event e = (Event)ePrime;
        try {
            equalType = (e.getType() == this.type);
            equalOwner = (e.getOwner() == this.owner);
            equalMessage = ((e.getMessage() == null && this.message == null) || e.getMessage().equals(this.message));
            equalOffer = ((e.getOffer() == null && this.offer == null) || this.offer.equals(e.getOffer()));
            equalPref = ((e.getPreference() == null && this.preference == null) || this.preference.equals(e.getPreference()));
            equalValue = (e.getValue() == this.value);
        } catch (NullPointerException n) {
            return false;
        }
        return (equalType && equalOwner && equalMessage && equalOffer && equalPref && equalValue);
    }
}