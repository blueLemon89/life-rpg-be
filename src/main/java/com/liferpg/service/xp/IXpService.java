package com.liferpg.service.xp;

import java.util.UUID;

public interface IXpService {


    void handleLevelUp(UUID CharacterId);
    void handleMultiLevelUp(UUID CharacterId);
}
