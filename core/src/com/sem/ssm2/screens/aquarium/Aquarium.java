package com.sem.ssm2.screens.aquarium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sem.ssm2.Game;
import com.sem.ssm2.client.Client;
import com.sem.ssm2.screens.GameScreen;
import com.sem.ssm2.screens.Screen;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.collection.RewardGenerator;
import com.sem.ssm2.structures.collection.collectibles.Collectible;
import com.sem.ssm2.structures.groups.GroupData;
import com.sem.ssm2.util.CollectibleDrawer;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Aquarium extends GameScreen {

    protected Game game;

    protected Table filler, layout;
    protected Stage stage;
    protected CollectibleDrawer collectibleDrawer;

    protected TextField ipTextField;
    protected TextField portTextField;
    protected TextButton connectButton;
    protected SelectBox<GroupData> groupSelectBox;

    protected int groupId;



    protected ArrayList<Fish> allFish;


    public Aquarium(Game game) {
        super(game);
    }


    @Override
    public Class<? extends Screen> previousScreen() {
        return null;
    }

    @Override
    public void loadAssets() {
        assets.load("images/FishA.png", Texture.class);
        assets.load("images/FishB.png", Texture.class);
        assets.load("images/FishC.png", Texture.class);
        assets.load("images/Rock1.png", Texture.class);
        assets.load("images/kelp.png", Texture.class);
        assets.load("images/rock2.png", Texture.class);
        assets.load("images/button.png", Texture.class);
        assets.load("images/button_disabled.png", Texture.class);
        assets.load("images/button_pressed.png", Texture.class);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        assets.generateFont("someFont", "fonts/NotoSans-Regular.ttf", parameter);
        parameter.size = 124;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        assets.generateFont("white_buttonFont", "fonts/Blenda Script.otf", parameter);
        parameter.color = new Color(71 / 255f, 37 / 255f, 2 / 255f, 1);
        parameter.size = 80;
        parameter.borderWidth = 0;
        assets.generateFont("brown_buttonFont", "fonts/Blenda Script.otf", parameter);
    }

    @Override
    public void show() {
        allFish =  new ArrayList<>();


        stage = new Stage();
        inputMultiplexer.addProcessor(stage);
        collectibleDrawer = new CollectibleDrawer(assets);
        stage.setDebugAll(true);
        filler = new Table();
        stage.addActor(filler);
        filler.setFillParent(true);
        layout = new Table();

        filler.setBackground(new TextureRegionDrawable(new TextureRegion(createMenuBackground())));

        filler.add(layout);

        layout.add(createLabel("Ip: "));
        layout.add(createIpTextField());
        layout.row();
        layout.add(createLabel("Port: "));
        layout.add(createPortTextField());
        layout.row();
        layout.add(createConnectButton()).colspan(2);
        layout.row();
        layout.add(createLabel("Group: "));
        layout.add(createGroupSelectBox());


        Collection collection = new Collection();
        RewardGenerator rewardGenerator = new RewardGenerator("");
        collection.add(rewardGenerator.generateCollectible(1));
        addCollection(collection);

        client.getRemoteStateChange().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if(arg != null && (boolean) arg) {
                    Gdx.app.log("Aquarium", "Connected to a remote server.");
                    client.getAllGroups(new ResponseHandler() {
                        @Override
                        public void handleResponse(Response response) {
                            if (response.isSuccess()) {
                                Gdx.app.log("Aquarium", "Received new groups.");
                                groupSelectBox.clearItems();
                                ArrayList<GroupData> list = (ArrayList<GroupData>) response.getData();
                                System.out.println("# of groups: " + list.size());
                                for (GroupData g : list) {
                                    System.out.println(g);
                                }
                                groupSelectBox.setItems(list.toArray(new GroupData[list.size()]));
                            }
                        }
                    });
                }
            }
        });
    }

    private Texture createMenuBackground() {
        Pixmap pixmap = new Pixmap(4,4, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(59 / 255f, 85 / 255f, 1, 1));
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Label createLabel(String text){
        return new Label(text, new Label.LabelStyle(assets.get("white_buttonFont", BitmapFont.class),Color.WHITE));
    }

    private TextField createIpTextField(){
        ipTextField = new TextField("127.0.0.1", new TextField.TextFieldStyle(
                assets.get("someFont", BitmapFont.class),
                Color.WHITE,
                new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.BLACK))),
                new BaseDrawable(),
                new BaseDrawable()));
        return ipTextField;
    }

    private TextField createPortTextField(){
        portTextField = new TextField("56789", new TextField.TextFieldStyle(
                assets.get("someFont", BitmapFont.class),
                Color.WHITE,
                new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.BLACK))),
                new BaseDrawable(),
                new BaseDrawable()));
        return portTextField;
    }

    private TextButton createConnectButton() {
        connectButton = new TextButton("Connect", getTextButtonStyle());
        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String ip = ipTextField.getText();
                String port = portTextField.getText();
                Gdx.app.log("Aquarium", "Connecting with: " + ip + ":" + port);
                if (Client.isValidIP(ip)){
                    client.setRemoteIP(ip);
                    client.setRemotePort(Integer.parseInt(port));
                    client.connectToRemoteServer();
                }
            }
        });
        return connectButton;
    }
    private TextButton.TextButtonStyle getTextButtonStyle() {
        Texture texture = assets.get("images/button.png");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * assets.getRatio(), texture.getHeight() * assets.getRatio());

        Sprite sprite2 = new Sprite(assets.get("images/button_pressed.png", Texture.class));
        sprite2.setSize(texture.getWidth() * assets.getRatio(), texture.getHeight() * assets.getRatio());

        Sprite disabled = new Sprite(assets.get("images/button_disabled.png", Texture.class));
        disabled.setSize(texture.getWidth() * assets.getRatio(), texture.getHeight() * assets.getRatio());

        return new TextButton.TextButtonStyle(
                new SpriteDrawable(sprite),
                new SpriteDrawable(sprite2),
                new SpriteDrawable(sprite2),
                assets.get("brown_buttonFont", BitmapFont.class)
        );
    }

    private SelectBox<GroupData> createGroupSelectBox() {
        groupSelectBox = new SelectBox<GroupData>(getSelectBoxStyle());
        groupSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Aquarium group", "Selected group: " + groupSelectBox.getSelected());
                groupId = groupSelectBox.getSelected().getGroupId();
                fillAquarium();
            }
        });

        return groupSelectBox;
    }

    private void fillAquarium() {
    }

    private SelectBox.SelectBoxStyle getSelectBoxStyle() {
        List.ListStyle listStyle = new List.ListStyle(
                assets.get("white_buttonFont", BitmapFont.class),
                Color.TEAL,
                Color.CORAL,
                new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.WHITE)))
        );

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle(
                new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.LIME))),
                new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.FOREST))),
                new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.MAGENTA))),
                new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.PURPLE))),
                new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.SLATE)))
        );

        return new SelectBox.SelectBoxStyle(
                assets.get("white_buttonFont", BitmapFont.class),
                Color.SALMON,
                new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.BROWN))),
                scrollPaneStyle,
                listStyle
        );
    }

    @Override
    public void render(float delta) {
        for(Fish fish : allFish) {
            fish.swim();
        }

        Gdx.gl.glClearColor(59 / 255f, 179 / 255f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public void addCollection(Collection collection) {
        if(collection != null) {
            for(Collectible collectible : collection) {
                Sprite sprite = collectibleDrawer.drawCollectible(collectible);
                sprite.setSize(
                        sprite.getTexture().getWidth() / 1.5f * assets.getRatio(),
                        sprite.getTexture().getHeight() / 1.5f * assets.getRatio()
                );
                Fish image = new Fish(new SpriteDrawable(sprite));
                allFish.add(image);
                stage.addActor(image);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(stage);
    }

    @Override
    public void unloadAssets() {

    }

    @Override
    public void dispose() {

    }
}
