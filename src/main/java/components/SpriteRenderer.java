package components;

import jade.Component;

public class SpriteRenderer extends Component {
    private boolean firstime = false;
    @Override
    public void start(){
        System.out.println("I am starting");
    }

    @Override
    public void update(float dt) {
        if (!firstime) {
            System.out.println("I am updating");
            firstime = true;
        }
    }
}
