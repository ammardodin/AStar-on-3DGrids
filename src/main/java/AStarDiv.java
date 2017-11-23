import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class AStarDiv implements AIModule {
  public List<Point> createPath(final TerrainMap map) {

    Comparator<Node> comparator = new NodeComparator(); //comparator for priority queue
    final decreaseKeyHeap open = new decreaseKeyHeap(comparator); //decrease key heap via hash and pq
    final HashMap<Point, Node> closed = new HashMap<Point, Node>(); //hashmap in case we need to pull it out again

    final Point startPoint = map.getStartPoint(); //start point
    final Point endPoint = map.getEndPoint(); //end point

    Node startNode = new Node(startPoint, null, 0.0, getHeuristic(map, startPoint, endPoint)); //create start node

    open.insert(startNode); //insert startNode into the open list

    while (!open.isEmpty()) {//loop while the openList is not empty

      Node u = open.pop(); //pop the node with the smallest f value
      closed.put(u.pt, u); //place the popped node into the closed list

      if (u.pt.equals(endPoint)) //if it's the goal node, then retrace the path
        return buildPath(u);

      Point[] neighbors = map.getNeighbors(u.pt); //neighbors of the node being expanded

      for (Point v : neighbors) {

        if (open.contains(v)) {//if its in the openList

          Node n = open.get(v);

          double oldCost = n.g; //old cost to get to v
          double newCost = u.g + map.getCost(u.pt, v); //cost to get to u, then go from u to v

          if (newCost < oldCost) {
            open.updateKey(n, u, newCost, getHeuristic(map, v, endPoint)); //reduce the key of v

          }

        }

        else {//either in closedlist or never explored

          if (closed.containsKey(v)) {//we need this in case the heuristic is not consistent

            Node n = closed.get(v);

            double oldCost = n.g;
            double newCost = u.g + map.getCost(u.pt, v);

            if (newCost < oldCost) {
              closed.remove(v);
              Node toAdd = new Node(v, u, newCost, getHeuristic(map, v, endPoint));
              open.insert(toAdd);

            }

          }

          else {//the node was never explored

            Node toAdd = new Node(v, u, u.g + map.getCost(u.pt, v), getHeuristic(map, v, endPoint));
            open.insert(toAdd);

          }

        }

      }

    }

    return null; //silence javac

  }

  private double getHeuristic(final TerrainMap map, final Point pt1, final Point pt2) {
    double h2 = map.getTile(pt2); //goal height
    double h1 = map.getTile(pt1); //current height

    double deltaD = Math.max(Math.abs(pt2.x - pt1.x), Math.abs(pt2.y - pt1.y)); //number of tiles away

    if (h1 > h2) {//we drop down, then walk to the goal
      return (deltaD - 1) * (h2 / (h2 + 1)) + (h1 / (h2 + 1));
    }

    else {//we walk at the same height, then jump to the goal
      return (deltaD - 1) * (h1 / (h1 + 1)) + (h1 / (h2 + 1));
    }

  }

  public ArrayList<Point> buildPath(Node goalNode) {
    ArrayList<Point> path = new ArrayList<Point>();

    while (goalNode != null) {
      path.add(goalNode.pt);
      goalNode = goalNode.parent;
    }

    Collections.reverse(path);

    return path;
  }

  public class Node {
    private Point pt;
    private Node parent;
    private double g;
    private double f;

    public Node(Point pt, Node parent, double g, double h) {
      this.pt = pt;
      this.parent = parent;
      this.g = g;
      this.f = g + h;
    }

  }

  public class NodeComparator implements Comparator<Node> {

    public int compare(Node a, Node b) {

      if (a.f > b.f) {
        return 1;
      } else if (a.f < b.f) {
        return -1;
      } else
        return 0;
    }
  }

  public class decreaseKeyHeap {

    private final PriorityQueue<Node> open; //actual priority queue
    private final HashMap<Point, Node> openHash; //for O(1) lookups

    public decreaseKeyHeap(Comparator<Node> comp) {
      this.open = new PriorityQueue<Node>(100, comp);
      this.openHash = new HashMap<Point, Node>();
    }

    public void insert(Node n) {
      this.open.add(n);
      this.openHash.put(n.pt, n);
    }

    public void updateKey(Node v, Node parent, double g, double h) {
      this.open.remove(v);
      this.openHash.remove(v.pt);
      Node updatedNode = new Node(v.pt, parent, g, h);
      this.open.add(updatedNode);
      this.openHash.put(v.pt, updatedNode);
    }

    public Node pop() {
      Node temp = this.open.poll();
      this.openHash.remove(temp.pt);
      return temp;
    }

    public boolean isEmpty() {
      return this.open.isEmpty();
    }

    public Node get(Point u) {
      return this.openHash.get(u);
    }

    public boolean contains(Point pt) {
      return this.openHash.containsKey(pt);
    }

  }

}