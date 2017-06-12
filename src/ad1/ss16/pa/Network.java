package ad1.ss16.pa;

import java.util.*;

public class Network {
    private int nodes;
    private int connections;
    private boolean cycle;
    BitSet visited;
    
    //adjazenzListe
    private ArrayList<TreeSet<Integer>> adjazenzListe;
    
    public Network(int n) {
        this.nodes = n;
        this.connections = 0;
        this.cycle = false;
        
        this.adjazenzListe = new ArrayList<>(n);
        for(int i = 0; i < this.nodes; i++){
            this.adjazenzListe.add(new TreeSet<>());
        }
    }
    
    public int numberOfNodes() {
        // gibt die Anzahl der Knoten zurueck
        return this.nodes;
    }
    
    public int numberOfConnections() {
        // gibt die Anzahl der Verbindungen zurueck
        return this.connections;
    }
    
    public void addConnection(int v, int w){
        // fuegt eine Verbindung zwischen v und w ein
        if(!this.adjazenzListe.get(v).contains(w) && v != w){
            //liste
            this.adjazenzListe.get(v).add(w);
            this.adjazenzListe.get(w).add(v);
            
            this.connections++;
        }
    }
    
    public void addAllConnections(int v){
        // fuegt Verbindungen von Knoten v zu allen anderen Knoten ein
        //matrix
        for(int i = 0; i < this.nodes; i++){
            if(i != v && !this.adjazenzListe.get(i).contains(v)){
                //liste
                this.adjazenzListe.get(v).add(i);
                this.adjazenzListe.get(i).add(v);
                
                this.connections++;
            }
        }
    }
    
    public void deleteConnection(int v, int w){
        // entfernt die Verbindung zwischen v und w, falls nicht verbunden, passiert nichts
        if(this.adjazenzListe.get(v).contains(w) && v != w) {
            //liste
            this.adjazenzListe.get(v).remove(w);
            this.adjazenzListe.get(w).remove(v);
            
            this.connections--;
        }
    }
    
    public void deleteAllConnections(int v){
        // entfernt alle Verbindungen von Knoten v
        //matrix
        for(int i = 0; i < this.nodes; i++){
            if(i != v && this.adjazenzListe.get(i).contains(v)){
                //liste
                this.adjazenzListe.get(i).remove(v);
                this.adjazenzListe.get(v).remove(i);
                
                this.connections--;
            }
        }
        
        //liste
        this.adjazenzListe.get(v).clear();
    }
    
    //Tiefensuche
    private void dfs(BitSet visited, int n){
        visited.set(n);
        
        for(int entry : this.adjazenzListe.get(n)){
            if(!visited.get(entry)){
                dfs(visited, entry);
            }
        }
    }
    
    public int numberOfComponents() {
        // gibt die Anzahl der "Subnetzwerke", die nicht miteinander verbunden sind zurueck
        BitSet visited = new BitSet(this.nodes);
        
        int components = 0;
        
        for(int i = 0; i < this.nodes; i++){
            if(!visited.get(i)){
                //Tiefensuche
                dfs(visited, i);
                components++;
            }
        }
        
        return components;
    }
    
    public void hasCycleDFS(BitSet visited, int begin, int parent) {
        visited.set(begin);
        
        for(int entry : this.adjazenzListe.get(begin)){
            if(visited.get(entry) && entry != parent){
                cycle = true;
            }
            if(!visited.get(entry)){
                hasCycleDFS(visited, entry, begin);
            }
        }
    }
    
    public boolean hasCycle(){
        this.cycle = false;
        BitSet visited = new BitSet(this.nodes);
        
        for(int i = 0; i < this.nodes; i++){
            if(!visited.get(i)){
                hasCycleDFS(visited, i, -1);
            }
        }
        
        return this.cycle;
    }
    
    public int minimalNumberOfConnections(int start, int end){
        if(start == end){
            return 0;
        }
        
        BitSet visited = new BitSet(this.nodes);
        int distance[] = new int[this.nodes];
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        
        visited.set(start);
        stack.push(start);
        
        while(!stack.isEmpty()){
            int next = stack.poll();
            
            for(int entry : this.adjazenzListe.get(next)){
                if(entry == end){
                    return distance[next] + 1;
                }
                if(!visited.get(entry)){
                    visited.set(entry);
                    distance[entry] = distance[next] + 1;
                    stack.offer(entry);
                }
            }
        }
        
        return -1;
    }
    
    //Tiefensuche
    private void dfsCritical(TreeSet<Integer> checkList, int n, int notAllowed){
        visited.set(n);
        if(checkList.contains(n)){
            checkList.remove(n);
        }
        
        if(!checkList.isEmpty()){
            for(int entry : this.adjazenzListe.get(n)){
                if(entry != notAllowed && !visited.get(entry)) {
                    dfsCritical(checkList, entry, notAllowed);
                }
                if(checkList.isEmpty()){
                    break;
                }
            }
        }
    }
    
    public List<Integer> criticalNodes() {
        // liefert eine Liste jener Knoten zurueck, die als kritisch eingestuft sind
        // dies ist der Fall, wenn durch Entfernung aller Verbindungen dieses Knotens auch andere Knoten isoliert sind
        List<Integer> critical = new LinkedList<Integer>(); //ap
        boolean critCycle = hasCycle();
        
        for(int i = 0; i < this.nodes; i++){
            if(!critCycle && this.adjazenzListe.get(i).size() > 2){
                critical.add(i);
            }else if(!this.adjazenzListe.get(i).isEmpty()){
                this.visited = new BitSet();
                this.visited.set(i);
                
                TreeSet<Integer> checkList = new TreeSet<>(this.adjazenzListe.get(i));
                
                dfsCritical(checkList, checkList.pollFirst(), i);
                
                if (!checkList.isEmpty()) {
                    critical.add(i);
                }
            }
        }
        
        return critical;
    }
}