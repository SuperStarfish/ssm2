package com.sem.ssm2.stroll;


import com.sem.ssm2.Game;
import com.sem.ssm2.client.Client;
import com.sem.ssm2.screens.QuizScreen;
import com.sem.ssm2.screens.Screen;
import com.sem.ssm2.screens.TapTheFishScreen;
import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.collection.RewardGenerator;
import com.sem.ssm2.structures.collection.collectibles.CollectibleFactory;

import java.util.ArrayList;
import java.util.Random;

public class Stroll {

    protected Game game;
    protected Client client;
    protected Random random;
    protected RewardGenerator rewardGenerator;
    protected Collection collection = new Collection();
    protected ArrayList<Class<? extends Screen>> eventScreens = new ArrayList<>();


    public Stroll(Game game) {
        random = new Random();
        this.game = game;
        client = game.getClient();
        rewardGenerator = new RewardGenerator(client.getCurrentPlayerData().getId());
        eventScreens.add(QuizScreen.class);
        eventScreens.add(TapTheFishScreen.class);
    }

    public void addRewards(int score) {
        collection.add(rewardGenerator.generateCollectible(score));
    }

    public Collection getStrollRewards() {
        return collection;
    }

    public Class<? extends Screen> getRandomEventScreen() {
        return eventScreens.get(random.nextInt(eventScreens.size()));
    }

}
