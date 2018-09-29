package com.zach.ddg3;

import com.zach.engine.Main;

/**
 * Created by Zach on 6/12/2018.
 */
public abstract class GameLevel
{
    //public String lvlName;

    public abstract void init(Main main);

    public abstract void update(Main main, float dt);

    public abstract void uninit();
}
