package coreKernel;

import graphicEngine.GraphicObject;
import physicEngine.PhysicObject;

public class GameObject {
    private PhysicObject physicObject;
    private GraphicObject graphicObject;
    public GameObject()
    {physicObject = null;
        graphicObject = null;}

    public void updateGameObject(){
        graphicObject.updatePosition(physicObject.getPosition().getX(),physicObject.getPosition().getY());
        if(getGraphicObject().getImageView() != null) graphicObject.rescaleImageView(physicObject.getWidth(),physicObject.getHeight());
    }
    public PhysicObject getPhysicObject() {
        return physicObject;
    }
    public void setPhysicObject(PhysicObject physicObject) {
        this.physicObject = physicObject;
    }
    public GraphicObject getGraphicObject() {
        return graphicObject;
    }
    public void setGraphicObject(GraphicObject graphicObject) {
        this.graphicObject = graphicObject;
    }
}
