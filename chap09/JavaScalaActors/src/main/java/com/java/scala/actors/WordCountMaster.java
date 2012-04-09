package com.java.scala.actors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import scala.actors.Actor;
import scala.actors.Exit;
import scala.actors.Future;

import com.javaapi.forscala.actors.Effect;
import com.javaapi.forscala.actors.ScalaActorWrapper;

public class WordCountMaster {
	
	@SuppressWarnings("unchecked")
	public List<Pair<String, Integer>> beginSorting(String docRoot, Integer numActors) {
		Actor master = ScalaActorWrapper.actor(new Effect<StartCounting>() {
			@Override
			public void e(Actor self, StartCounting msg) {
				List<Actor> workers = createWorkers(self, msg.numActors);
				String[] fileNames = new File(msg.docRoot).list();
				List<Future> futures = sendMessages(msg, workers, fileNames);
				List<Pair<String, Integer>> result = new ArrayList<Pair<String,Integer>>();
				for(Future f: futures) {
					WordCount c = (WordCount) f.apply();
					result.add(new Pair(c.fileName, c.count));
				}
				Collections.sort(result, new Comparator<Pair<String, Integer>>() {
					@Override
					public int compare(Pair<String, Integer> o1,
							Pair<String, Integer> o2) {
						return o1._2.compareTo(o2._2);
					}
				});
				self.reply(result);
				exitActors(self, workers);
			}

			private List<Future> sendMessages(StartCounting msg,
					List<Actor> workers, String[] fileNames) {
				int cnt = 0;
				List<Future> futures = new ArrayList<Future>();
				for(int i = 0; i < fileNames.length ; i++) {
					Future future = workers.get(cnt % msg.numActors).$bang$bang(new FileToCount(msg.docRoot + fileNames[i]));
					futures.add(future);
					cnt += 1;
				}
				return futures;
			}
			
			private void exitActors(Actor self, List<Actor> workers) {
				for (Actor worker : workers) {
					worker.$bang(new Exit(self, "done"));
				}
				self.$bang(new Exit(self, "done"));
			}			
		});
		Future<Object> future = master.$bang$bang(new StartCounting(docRoot, numActors));
		return (List<Pair<String, Integer>>)future.apply();
	}

	private List<Actor> createWorkers(Actor self, Integer numActors) {
		List<Actor> workers = new ArrayList<Actor>();
		for(int i = 0; i < numActors; i++) {
			Actor worker = createWorker();
			workers.add(worker);
		}
		return workers;
	}

	private Actor createWorker() {
		return ScalaActorWrapper.actor(new Effect<FileToCount>() {
			@Override
			public void e(Actor self, FileToCount msg) {
				int count = countWords(msg);
				self.reply(new WordCount(msg.fileName, count));
			}

			private int countWords(FileToCount msg) {
				try {
					int count = 0;
					Scanner s = new Scanner(new File(msg.fileName)).useDelimiter(" ");
					while(s.hasNext()) {
						s.next();
						count++; 
					}
					return count;
				} catch (FileNotFoundException e1) {
					throw new RuntimeException(e1);
				}
			}
		});
	}
}

class Pair<A, B> {
	public Pair(A _1, B _2) {
		this._1 = _1;
		this._2 = _2;
	}
	public A _1;
	public B _2;
}

class StartCounting{
	public String docRoot;
	public Integer numActors;
	public StartCounting(String docRoot, Integer numActors) {
		this.docRoot = docRoot;
		this.numActors = numActors;
	}
}

class FileToCount {
	public FileToCount(String fileName) {
		this.fileName = fileName;
	}
	public String fileName;
}

class WordCount {
	public WordCount(String fileName, Integer count) {
		this.fileName = fileName;
		this.count = count;
	}
	public String fileName;
	public Integer count;
}




