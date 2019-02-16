import java.util.ArrayList;
import java.util.List;

public class StateNode {
    private StateNode parent;
    private State state;
    private List<StateNode> children;
    private Action action;

    public StateNode(State state) {
        this.state = state;
        this.parent = null;
        this.action = null;
        this.children = new ArrayList<StateNode>();
    }

    public StateNode(StateNode parent, State state, Action action) {
        this.parent = parent;
        this.state = state;
        this.action = action;
    }

    public StateNode getParent() {
        return parent;
    }

    public void setParent(StateNode parent) {
        this.parent = parent;
    }

    public List<StateNode> getChildren() {
        return children;
    }

    public void setChildren(List<StateNode> children) {
        this.children = children;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
