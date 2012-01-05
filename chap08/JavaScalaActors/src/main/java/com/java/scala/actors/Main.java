package com.java.scala.actors;

import scala.actors.Actor;
import scala.actors.Exit;

import com.javaapi.forscala.actors.Effect;
import com.javaapi.forscala.actors.ScalaActorWrapper;


class Name {
	private String name;
	public Name(String name) { this.name = name; }
	public String getName() { return name; } 
}

public class Main {
	public static void main(String[] args) throws Exception {
		Actor greetingsActor = ScalaActorWrapper.actor(new Effect<Name>(){
			@Override
			public void e(Actor self, Name n) {
				System.out.println("Hello " + n.getName());
			}
			
		});
		
		greetingsActor.$bang(new Name("Nilanjan"));
		greetingsActor.$bang(new Exit(greetingsActor, "done"));
	}
}
