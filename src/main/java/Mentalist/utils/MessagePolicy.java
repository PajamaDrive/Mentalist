package Mentalist.utils;

public interface MessagePolicy {
    String getProposalLang(History paramHistory, GameSpec paramGameSpec);

    String getAcceptLang(History paramHistory, GameSpec paramGameSpec);

    String getRejectLang(History paramHistory, GameSpec paramGameSpec);

    String getVHAcceptLang(History paramHistory, GameSpec paramGameSpec);

    String getVHRejectLang(History paramHistory, GameSpec paramGameSpec);

    @Deprecated
    String getMessageResponse(History paramHistory, GameSpec paramGameSpec);

    Event getVerboseMessageResponse(History paramHistory, GameSpec paramGameSpec, Event paramEvent);
}
