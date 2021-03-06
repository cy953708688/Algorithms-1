package Number_4_02;

import edu.princeton.cs.algs4.Topological;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.DirectedEdge;

/**
 * P427 算法 4.10 无环加权有向图的最短路径算法， 适用于加权有向无环图，能够处理负权重的边以及找出最长路径
 * 处理加权有向无环图比Dijkstra算法快
 *
 * @author he args[0]:tinyEWDAG.txt args[1]:5
 */
public class AcyclicSP {
	private double distTo[];// 保存权重，最短路径的总权重，distTo[w]表示起点到w最短路径的总权重
	private DirectedEdge edgeTo[];// 最短路径的边
	private double longDist[];// 保存最长路径的总权重
	private DirectedEdge longEdge[];// 最长路径的边

	// s为起点
	public AcyclicSP(EdgeWeightedDigraph G, int s) {
		// 最短路径
		distTo = new double[G.V()];
		edgeTo = new DirectedEdge[G.V()];

		// 最长路径
		longDist = new double[G.V()];
		longEdge = new DirectedEdge[G.V()];

		for (int i = 0; i < G.V(); i++) {
			distTo[i] = Double.POSITIVE_INFINITY;// 保存极大的double值
			longDist[i] = Double.MIN_VALUE;// 保存极小的double值
		}
		distTo[s] = 0.0;
		longDist[s] = 0.0;
		Topological top = new Topological(G);
		for (int v : top.order()) {
			relax(G, v);
			shrink(G, v);
		}
	}

	// 保证distTo[w]从起点到指定顶点最小的总权重
	private void relax(EdgeWeightedDigraph G, int v) {
		for (DirectedEdge e : G.adj(v)) {
			int w = e.to();
			if (distTo[w] > distTo[v] + e.weight()) {
				distTo[w] = distTo[v] + e.weight();
				edgeTo[w] = e;
			}
		}
	}

	// 保证longDist[w]从起点到指定顶点最大的总权重
	private void shrink(EdgeWeightedDigraph G, int v) {
		for (DirectedEdge e : G.adj(v)) {
			int w = e.to();
			if (longDist[w] < longDist[v] + e.weight()) {
				longDist[w] = longDist[v] + e.weight();
				longEdge[w] = e;
			}
		}
	}

	/**
	 * 返回起点到顶点v最短路径的总权重
	 * 
	 * @param v
	 * @return
	 */
	public double distTo(int v) {
		return distTo[v];
	}

	/**
	 * 返回起点到顶点v最长路径的总权重
	 * 
	 * @param v
	 * @return
	 */
	public double longDist(int v) {
		return longDist[v];
	}

	/**
	 * 是否存在顶点s到v的路径
	 * 
	 * @param v
	 * @return
	 */
	public boolean hasPathTo(int v) {
		return distTo[v] < Double.POSITIVE_INFINITY;
	}

	/**
	 * 从起点到v的最短路径
	 * 
	 * @param v
	 * @return
	 */
	public Iterable<DirectedEdge> pathTo(int v) {
		if (!hasPathTo(v))
			return null;
		Stack<DirectedEdge> path = new Stack<DirectedEdge>();
		for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
			path.push(e);
		return path;
	}

	/**
	 * 从起点到顶点v最长路径
	 * 
	 * @param v
	 * @return
	 */
	public Iterable<DirectedEdge> longPathTo(int v) {
		if (!hasPathTo(v))
			return null;
		Stack<DirectedEdge> path = new Stack<DirectedEdge>();
		for (DirectedEdge e = longEdge[v]; e != null; e = longEdge[e.from()])
			path.push(e);
		return path;

	}

	public static void main(String[] args) {
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(new In(args[0]));
		int s = Integer.valueOf(args[1]);// 起点
		AcyclicSP sp = new AcyclicSP(G, s);

		for (int t = 0; t < G.V(); t++) {
			System.out.print(s + " to " + t);
			System.out.printf(" (%.2f): ", sp.distTo(t));
			if (sp.hasPathTo(t))
				for (DirectedEdge e : sp.pathTo(t))
					System.out.print(e + " ");
			System.out.println();
		}
		System.out.println("---------------------最长路径-------------------");
		for (int t = 0; t < G.V(); t++) {
			System.out.print(s + " to " + t);
			System.out.printf(" (%.2f): ", sp.longDist(t));
			if (sp.hasPathTo(t))
				for (DirectedEdge e : sp.longPathTo(t))
					System.out.print(e + " ");
			System.out.println();
		}
		
		
	}

}
