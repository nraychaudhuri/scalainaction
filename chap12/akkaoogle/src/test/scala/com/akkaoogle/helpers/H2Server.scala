package com.akkaoogle.helpers
import org.h2.tools.Server

object H2Server {
	
	private var server: Option[Server] = None
	def start(): Unit = {
		server = Some(Server.createTcpServer().start()) 
	}
	
	def stop(): Unit = {
		server foreach {s =>  s.stop() }
	}
}
