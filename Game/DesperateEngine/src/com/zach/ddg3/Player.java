package com.zach.ddg3;

import com.ivan.xinput.XInputAxesDelta;
import com.ivan.xinput.XInputButtonsDelta;
import com.ivan.xinput.XInputComponentsDelta;
import com.ivan.xinput.XInputDevice;
import com.zach.ddg3.components.AABBComponent;
import com.zach.engine.Main;
import com.zach.engine.Renderer;

import java.awt.event.KeyEvent;

public class Player extends Object
{
    public static boolean isGoose = false;
    public static int playerNumber;
    public XInputDevice device;
    private XInputComponentsDelta delta;
    private XInputButtonsDelta buttons;
    private XInputAxesDelta axes;

    private boolean collidingTop = false;
    private boolean collidingBottom = false;
    private boolean collidingLeft = false;
    private boolean collidingRight = false;

    private float lStickX = 0f;
    private float lStickY = 0f;
    private float rStickX = 0f;
    private float rStickY = 0f;

    private int color = (int)(Math.random() * Integer.MAX_VALUE);

    public Player(String name, int width, int height, String path, int totalFrames, float frameLife, int playerNumber)
    {
        super(name, width, height, path, totalFrames, frameLife);

        this.playerNumber = playerNumber;
        device = GameManager.deviceManager.devices[playerNumber];
        delta = GameManager.deviceManager.deltas[playerNumber];
        buttons = GameManager.deviceManager.buttons[playerNumber];
        axes = GameManager.deviceManager.axes[playerNumber];
        //GameManager.deviceManager.addComponents(device, delta, buttons, axes);
        this.addComponent(new AABBComponent(this));

        //this.paddingTop = -30;
        //this.paddingSide = 0;
    }

    @Override
    public void update(Main main, GameManager gameManager, float dt)
    {
        collidingTop = false;

        if(device.poll())
        {
            lStickX += axes.getLXDelta();
            lStickY += axes.getLYDelta();
            rStickX += axes.getRXDelta();
            rStickY += axes.getRYDelta();

            moveController(dt);
            look();

            //System.out.println(offsetPos.x);
           /*this.offsetPos.x = main.getInput().getMouseX() - this.width;
           this.offsetPos.y = main.getInput().getMouseY() + this.height + 180;*/
        }
        else
        {
            moveKeyboard(main,dt);
        }

        this.offsetPos.x = (int)(this.position.x - (this.width / 2) + 320);
        this.offsetPos.y = (int)(this.position.y - (this.height / 2) + 180);

        this.updateComponents(main, gameManager, dt);
    }

    @Override
    public void render(Main main, Renderer r)
    {
        super.render(main, r);
        //r.drawFillRectangle((int)offsetPos.x, (int)offsetPos.y, width, height, color);
        this.renderComponents(main, r);
    }

    @Override
    public void collision(GameObject other)
    {
        if(other.getTag().equalsIgnoreCase("Wall"))
        {
            AABBComponent myC = (AABBComponent)this.findComponent("aabb");
            AABBComponent otherC = (AABBComponent)other.findComponent("aabb");

            if(myC.getCenterY() < otherC.getCenterY())
            {
                int distance = myC.getHalfHeight() + otherC.getHalfHeight() - (otherC.getCenterY() - myC.getCenterY());
                position.y -= distance;
                offsetPos.y -= distance;
                myC.setCenterY(myC.getCenterY() - distance);
                collidingTop = true;
            }
        }
    }

    public void moveController(float dt)
    {
        //Left
        if (lStickX > 0.4f)
        {
            if(!collidingRight)
            {
                this.position.x -= 100f * dt;
            }
            if (rStickX < 0.4f && rStickX > -0.4f)
            {
                this.setFrame(6);
            }
        }
        //Right
        if (lStickX < -0.4f)
        {
            if(!collidingLeft)
            {
                this.position.x += 100f * dt;
            }
            if (rStickX < 0.4f && rStickX > -0.4f) {
                this.setFrame(2);
            }
        }

        //Down
        if (lStickY > 0.4f)
        {
            if(!collidingTop)
            {
                this.position.y += 100f * dt;
            }
            if (rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(0);
            }
        }

        //Up
        if (lStickY < -0.4f)
        {
            if(!collidingBottom)
            {
                this.position.y -= 100f * dt;
            }
            if (rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(4);
            }
        }

        if(lStickY < 0.4f && lStickY > -0.4f)
        {
            //Idle on Y
            collidingTop = false;
        }

        //Changes frame of animation on the diagonals if the right thumbstick is not being used
        if (lStickX > 0.4f && lStickY < -0.4f)
        {
            if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f)
            {
                this.setFrame(5);
            }
        }
        if (lStickX > 0.4f && lStickY > 0.4f) {
            if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(7);
            }
        }
        if (lStickY > 0.4f && lStickX < -0.4f) {
            if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(1);
            }
        }
        if (lStickY < -0.4f && lStickX < -0.4f) {
            if (rStickX < 0.4f && rStickX > -0.4f && rStickY < 0.4f && rStickY > -0.4f) {
                this.setFrame(3);
            }
        }
    }

    public void look()
    {
        if (rStickX > 0.4f) {
            //Left
            this.setFrame(6);
        }
        if (rStickX < -0.4f) {
            //Right
            this.setFrame(2);
        }
        if (rStickY > 0.4f) {
            //Down
            this.setFrame(0);
        }
        if (rStickY < -0.4f) {
            //Up
            this.setFrame(4);
        }
        if (rStickX > 0.4f && rStickY < -0.4f) {
            //Left and Up
            this.setFrame(5);
        }
        if (rStickX > 0.4f && rStickY > 0.4f) {
            //Left and Down
            this.setFrame(7);
        }
        if (rStickY > 0.4f && rStickX < -0.4f) {
            //Right and Down
            this.setFrame(1);
        }
        if (rStickY < -0.4f && rStickX < -0.4f) {
            //Right and Up
            this.setFrame(3);
        }
    }

    public void moveKeyboard(Main main, float dt)
    {
        if (main.getInput().isKey(KeyEvent.VK_A))
        {
            this.position.x += -100f * dt;
            this.setFrame(6);

        }
        if (main.getInput().isKey(KeyEvent.VK_D))
        {
            this.position.x += 100f * dt;
            this.setFrame(2);
        }

        if (main.getInput().isKey(KeyEvent.VK_S))
        {
            this.position.y += 100f * dt;
            this.setFrame(0);
        }
        if (main.getInput().isKey(KeyEvent.VK_W))
        {
            this.position.y +=  -100f * dt;
            this.setFrame(4);
        }

        //Changes frame of animation on the diagonals if the right thumbstick is not being used
        if (main.getInput().isKey(KeyEvent.VK_A) && main.getInput().isKey(KeyEvent.VK_W))
        {
            this.setFrame(5);
        }
        if (main.getInput().isKey(KeyEvent.VK_A) && main.getInput().isKey(KeyEvent.VK_S))
        {
            this.setFrame(7);
        }
        if (main.getInput().isKey(KeyEvent.VK_D) && main.getInput().isKey(KeyEvent.VK_S))
        {
            this.setFrame(1);
        }
        if (main.getInput().isKey(KeyEvent.VK_D) && main.getInput().isKey(KeyEvent.VK_W))
        {
            this.setFrame(3);
        }

        //VERY IMPORTANT
        /*if(this.position.x >= 320 || this.position.x <= -320)
        {
            moveDir.x *= -1;
        }
        if(this.position.y >= 180 || this.position.y <= -180)
        {
            moveDir.y *= -1;
        }*/
    }
}
