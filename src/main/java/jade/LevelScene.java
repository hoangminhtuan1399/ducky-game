package jade;

public class LevelScene extends Scene{
    public LevelScene(){
   System.out.println("insede level scene");
   Window.get().r=0;
   Window.get().g=3;
   Window.get().b=0;
    }
    @Override
    public void update(float dt) {

    }
}
