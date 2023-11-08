import java.util.*;

public class HashTableChain<K, V> implements KWHashMap<K, V> {
    
	public static class Entry<K, V> {
    	private final K key;
    	private V value;
    	public Entry(K key, V value) {
        	this.key = key;
        	this.value = value;
    	}
    	public V getValue() {
        	return value;
    	}
    	public K getKey() {
        	return key;
    	}
    	public V setValue(V val) {
        	V PrevValue = value;
        	value = val;
        	return PrevValue;
    	}
	}

	
	private LinkedList<Entry<K, V>>[] HashTable;
	private int Keynum; 
	private static final int MAXCAP = 100; 
	private static final double MAXLOAD = 1.0; 
	private int hashCount = 0; 	
    
    @SuppressWarnings("unchecked")
	public HashTableChain() {
    	HashTable = new LinkedList[MAXCAP];
    	Keynum = 0;
	}

    @SuppressWarnings("unchecked")
	public HashTableChain(int cap) {
    	HashTable = new LinkedList[cap];
    	Keynum = 0;
	}

	public V get(Object key) {
    	int index = key.hashCode() % HashTable.length;
    	if(index < 0) {
        	index += HashTable.length;
    	}
    	if(HashTable[index] == null) {
        	return null;
    	}
    	for(Entry<K, V> nextItem: HashTable[index]) {
        	if(nextItem.getKey().equals(key)) {
            	return nextItem.getValue();
        	}
    	}
    	return null;
	}


	public boolean isEmpty() {
    	return Keynum != 0;
	}

	public V put(K key, V value) {
    	int index = key.hashCode() % HashTable.length;
    	if(index < 0) {
        	index += HashTable.length;
    	}
    	if(HashTable[index] == null) {
        	HashTable[index] = new LinkedList<>();
    	}
    	for(Entry<K, V> nextItem: HashTable[index]) {
        	if(nextItem.getKey().equals(key)) {
            	V oldVal = nextItem.getValue();
            	nextItem.setValue(value);
            	return oldVal;
        	}
    	}

    	HashTable[index].addFirst(new Entry<>(key, value));
    	Keynum++;
    	if(Keynum > (MAXLOAD * HashTable.length)) {
        	hashCount++;
        	rehash();
    	}
    	return null;
	}

	public V remove(Object key) {
    	int index = key.hashCode() % HashTable.length;
    	if(index < 0) {
        	index += HashTable.length;
    	}
    	if(HashTable[index] == null) {
        	return null;
    	}

    	Iterator<Entry<K, V>> entries = HashTable[index].iterator();
    	Entry<K, V> currentEntry;
    	while(entries.hasNext()) {
        	currentEntry = entries.next();
        	if(currentEntry.getKey().equals(key)) {
            	V oldVal = currentEntry.getValue();
            	entries.remove(); 
            	Keynum--;
            	if(HashTable[index].size() == 0) {
                	HashTable[index] = null;
            	}
            	return oldVal;
        	}
    	}
    	return null;
	}

	public int size() {
    	return Keynum;
	}

	public int hashCount() {
    	return hashCount;
	}

    @SuppressWarnings("unchecked")
	private void rehash() {
    	LinkedList<Entry<K, V>>[] oldTable = HashTable;
    	int table1 = 2*oldTable.length + 1;
    	if (isPrime(table1) == true){
        	HashTable = new LinkedList[table1];
    	}
    	else
    	{
        	for (int i = 0; i < MAXCAP; i++){
            	if(isPrime(table1 + i) == true){
                	HashTable = new LinkedList[table1 + i];
            	}
        	}
    	}
   	 
    	Keynum = 0;
    	for(int i = 0; i < oldTable.length; i++) {
        	if(oldTable[i] != null) {
            	for(Entry<K, V> nextItem: oldTable[i]) {
                	put(nextItem.getKey(), nextItem.getValue());
            	}
        	}
    	}
	}

	public static boolean isPrime(int num){
    	if (num == 2 || num == 3){
        	return true;
    	}

    	if (num % 2 == 0){
        	return false;
    	}

    	int sqrt = (int) Math.sqrt (num) + 1;
    	for (int i = 3; i < sqrt; i +=2){
        	if (num % i == 0) {
            	return false;
        	}
    	}
    	return true;

	}
}

