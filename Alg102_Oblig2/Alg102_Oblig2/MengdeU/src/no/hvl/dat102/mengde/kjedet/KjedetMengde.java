package no.hvl.dat102.mengde.kjedet;

//********************************************************************
// Kjedet implementasjon av en mengde. 
//********************************************************************
import java.util.*;

import no.hvl.dat102.mengde.adt.*;
import no.hvl.dat102.mengde.tabell.TabellMengde;

public class KjedetMengde<T> implements MengdeADT<T> {
	private static Random rand = new Random();
	private int antall; // antall elementer i mengden
	private LinearNode<T> start;

	/**
	 * Oppretter en tom mengde.
	 */
	public KjedetMengde() {
		antall = 0;
		start = null;
	}//

	@Override
	public void leggTil(T element) {
		if (!(inneholder(element))) {
			LinearNode<T> node = new LinearNode<T>(element);
			node.setNeste(start);
			start = node;
			antall++;
		}
	}

	@Override
	public void leggTilAlle(MengdeADT<T> m2) {
		Iterator<T> teller = m2.oppramser();
		while (teller.hasNext()) {
			leggTil(teller.next());
		}
	}

	@Override
	public T fjernTilfeldig() {
		LinearNode<T> forgjenger, aktuell;
		T resultat = null;
		if (!erTom()) {
			int valg = rand.nextInt(antall) + 1;
			if (valg == 1) {
				resultat = start.getElement();
				start = start.getNeste();
			} else {
				forgjenger = start;
				for (int nr = 2; nr < valg; nr++) {
					forgjenger = forgjenger.getNeste();
				}
				aktuell = forgjenger.getNeste();
				resultat = aktuell.getElement();
				forgjenger.setNeste(aktuell.getNeste());
			}
			antall--;
		} // if
		return resultat;
	}//

	@Override
	public T fjern(T element) {
		boolean funnet = false;
		LinearNode<T> forgjenger, aktuell;
		aktuell=start;
		forgjenger=null;
		T resultat = null;
		
		while(!funnet && !(aktuell.getElement()==null)) {
		if(!erTom()) {
			if (aktuell.getElement().equals(element)){
				if(aktuell==start) {
			resultat=aktuell.getElement();
			aktuell=null;
			funnet=true;
			antall--;
			
					} else {
			aktuell=forgjenger.getNeste();
			forgjenger.setNeste(aktuell.getNeste());
			resultat=aktuell.getElement();
			funnet=true;
			antall--;
				}
			}
		  }
		}
		return resultat;
	}//
	

	/******************************************************************
	Returnerer en streng som representerer mengden. 
	******************************************************************/
	public String toString(){// For klassen KjedetMengde
		String resultat = ""; 
		LinearNode<T> aktuell = start;    
		while(aktuell != null){
			resultat += aktuell.getElement().toString() + "\t";
			aktuell = aktuell.getNeste();
			}
		return resultat;
		
	}


	@Override
	public MengdeADT<T> union(MengdeADT<T> m2) {
		MengdeADT<T> begge = new KjedetMengde<T>();
		LinearNode<T> aktuell = start;
		T element = null;
		while(aktuell!= null){ // ubetinget innsetting av m1(this) i begge
		((KjedetMengde<T>)begge).settInn(aktuell.getElement());
		aktuell=aktuell.getNeste();
		}

		Iterator<T> teller = m2.oppramser(); // Se Iteratorklassen for KjedetMengde
		while (teller.hasNext()){ // Test p� om flere elementer i m2
		element = teller.next();
		if(!inneholder(element)) {
		((KjedetMengde<T>)begge).settInn(element);
		}
		}
		return begge;
	}//

	@Override
	public MengdeADT<T> snitt(MengdeADT<T> m2) {
		MengdeADT<T> snittM = new KjedetMengde<T>();
		T element = null;
		LinearNode<T> aktuell=start;
		while(aktuell!= null){ // ubetinget innsetting av m1(this) i begge
			((KjedetMengde<T>)snittM).settInn(aktuell.getElement());
			aktuell=aktuell.getNeste();
			}
		 Iterator<T> teller=((KjedetMengde<T>) m2).oppramser();
		 while(teller.hasNext()) {
			 element=teller.next();
			 if(!this.inneholder(element)) {
					snittM.fjern(element);
					}
					else {
						snittM.leggTil(element);
						}
				}
		return snittM;
	}

	

	@Override
	public MengdeADT<T> differens(MengdeADT<T> m2) {
		MengdeADT<T> differensM = new KjedetMengde<T>();
		T element;
		LinearNode<T> aktuell=start;
		while(aktuell!= null){ // ubetinget innsetting av m1(this) i begge
			((KjedetMengde<T>)differensM).settInn(aktuell.getElement());
			aktuell=aktuell.getNeste();
			}
		Iterator <T> teller= ((KjedetMengde<T>) m2).oppramser();
		
		
		while(teller.hasNext()) {
			element=teller.next();
			if(this.inneholder(element)) {
			differensM.fjern(element);
			}
			else {
				differensM.leggTil(element);
				}
		}
		
		
		
		return differensM;
	}

	@Override
	public boolean inneholder(T element) {
		boolean funnet = false;
		LinearNode<T> aktuell = start;
		for (int soek = 0; soek < antall && !funnet; soek++) {
			if (aktuell.getElement().equals(element)) {
				funnet = true;
			} else {
				aktuell = aktuell.getNeste();
			}
		}
		return funnet;
	}

	@Override
	public boolean equals(MengdeADT<T> m2) {
		boolean likeMengder = true;
		T element = null;
	if (antall==m2.antall()) {
			
			Iterator<T> teller=m2.oppramser();
			
			while(teller.hasNext()) {
				
			element=teller.next();	
			if (!inneholder(element)) {
				likeMengder=false;
			}		
			
		} 
			
		} else {
			likeMengder=false;
		}
		//Fyll ut
		return likeMengder;
	}

	@Override
	public boolean erTom() {
		return antall == 0;
	}

	@Override
	public int antall() {
		return antall;
	}

	@Override
	public Iterator<T> oppramser() {
		return new KjedetIterator<T>(start);
	}
	
	@Override
	public boolean undermengde(MengdeADT<T> m2) {
		boolean erUnderMengde = true;
		//Fyll ut
		return erUnderMengde;
	}
	
	private void settInn(T element) {
		LinearNode<T> nyNode = new LinearNode<T>(element);
		nyNode.setNeste(start);
		start = nyNode;
		antall++;
	}

}// class
