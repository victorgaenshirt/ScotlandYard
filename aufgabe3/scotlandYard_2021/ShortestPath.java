// O. Bittel;
// 01.04.2021

package shortestPath;


import shortestPath.directedGraph.*;
import sim.SYSimulation;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {
	
	SYSimulation sim = null;
	
	Map<V,Double> dist; 		// Distanz für jeden Knoten
	Map<V,V> pred; 				// Vorgänger für jeden Knoten
	IndexMinPQ<V,Double> cand; 	// Kandidaten als PriorityQueue PQ
	// ...
	DirectedGraph<V> graph;
	Heuristic<V> heur;

	/**
	 * Konstruiert ein Objekt, das im Graph g k&uuml;rzeste Wege 
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch 
	 * mit dem Dijkstra-Verfahren.
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 * dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		dist = new HashMap<>();
		pred = new HashMap<>();
		cand = new IndexMinPQ<>();
		// ...
		this.graph = g;
		this.heur = h;


	}

	/**
	 * Diese Methode sollte nur verwendet werden, 
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p><blockquote><pre>
	 *    if (sim != null)
	 *       sim.visitStation((Integer) v, Color.blue);
	 * </pre></blockquote>
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {
		if(heur == null){
			dijkstra(s,g);
		} else {
			astar(s,g);
		}

	}

	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		// ...
		return null;
	}

	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
		// ...
		return 0.0;
	}

	private void dijkstra(V s, V g){
		for(var v : graph.getVertexSet()){
			dist.put(v, Double.MAX_VALUE);
			pred.put(v, null);
		}
		dist.put(s,0.);
		cand.add(s,0.);
		while(!cand.isEmpty()){
			double d = cand.getMinValue();
			V v = cand.removeMin();
			for(var w : graph.getSuccessorVertexSet(v)){
				double newdistance = dist.get(v) + graph.getWeight(v,w);
				if(dist.get(w) == Double.MAX_VALUE){
					pred.put(w,v);
					dist.put(w, newdistance);
					cand.add(w,newdistance);
				} else if (newdistance < dist.get(w)){
					pred.put(w,v);
					dist.put(w, newdistance);
					cand.change(w, newdistance);
				}
			}
		}
	}

	private boolean astar(V s, V g){
		for(var v : graph.getVertexSet()){
			dist.put(v, Double.MAX_VALUE);
			pred.put(v, null);
		}
		dist.put(s,0.);
		cand.add(s,0. + heur.estimatedCost(s,g));

		while(!cand.isEmpty()){
			double d = cand.getMinValue();
			V v = cand.removeMin();
			if(s == g) return true;
			for(var w : graph.getSuccessorVertexSet(v)){
				double newdistance = dist.get(v) + graph.getWeight(v,w);
				if(dist.get(w) == Double.MAX_VALUE){
					pred.put(w,v);
					dist.put(w, newdistance);
					cand.add(w,newdistance + heur.estimatedCost(s,g));
				} else if (newdistance < dist.get(w)) {
					pred.put(w,v);
					dist.put(w, newdistance);
					cand.change(w, newdistance + heur.estimatedCost(s,g));
				}
			}
		}
		return false;
	}

}
