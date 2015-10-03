package com.sem.ssm2.stroll;


import com.sem.ssm2.Game;
import com.sem.ssm2.screens.QuizScreen;
import com.sem.ssm2.screens.Screen;
import com.sem.ssm2.screens.TapTheFishScreen;
import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.collection.collectibles.CollectibleFactory;
import com.sem.ssm2.util.RewardUtil;

import java.util.ArrayList;
import java.util.Random;

public class Stroll {

    protected Game game;
    protected Collection collection = new Collection();
    protected Random random = new Random();
    protected CollectibleFactory collectibleFactory = new CollectibleFactory();
    protected ArrayList<Class<? extends Screen>> eventScreens = new ArrayList<>();

    // Moet veranderd worden in goede rewards
    protected int reward = 0;


    public Stroll(Game game) {
        this.game = game;
        eventScreens.add(QuizScreen.class);
        eventScreens.add(TapTheFishScreen.class);
    }


    /**
     * Moet veranderd worden in goede rewards.
     * @param score
     */
    public void addRewards(int score) {
        reward++;
        System.out.println("IMPLEMENT REWARD FUNCTION!!!");
        System.out.println("IMPLEMENT REWARD FUNCTION!!!");
        System.out.println("IMPLEMENT REWARD FUNCTION!!!");
        System.out.println("IMPLEMENT REWARD FUNCTION!!!");
        System.out.println("IMPLEMENT REWARD FUNCTION!!!");
    }

    /**
     * Moet veranderd worden in goede rewars.
     * @return
     */
    public int getStrollRewards() {
        return reward;
    }

    public Class<? extends Screen> getRandomEventScreen() {
        Random random = new Random();
        return eventScreens.get(random.nextInt(eventScreens.size()));
    }

}
