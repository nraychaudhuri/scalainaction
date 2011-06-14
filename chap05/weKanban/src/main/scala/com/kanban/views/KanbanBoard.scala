package com.kanban.views

import com.kanban.models._

object KanbanBoard {
  def apply() = 
  <html>
  <head>{header}</head>
  <body>
    <h2 class="header">weKanban: Simple Kanban board</h2>
    <span id="message" class="message clearFloat"></span>
    <br/>
    <span class="linkLabel clearFloat"><a href="/card/create">[Add new story to Ready Phase]</a></span>
    <div class="phase" id="readyPhase">
      <h3 class="message" title="Stories ready for development. Limit is set to 3">Ready [3]</h3>
      {stories("ready")}
    </div>
    <div class="phase" id="devPhase">
      <h3 class="message" title="Stories in progress. Limit is set to 2">Dev [2]</h3>
      {stories("dev")}
    </div>
    <div class="phase" id="testPhase">
      <h3 class="message" title="Stories that are tested. Limit is set to 2">Test [2]</h3>
      {stories("test")}
    </div>
    <div class="phase" id="deployPhase" title="Ready for production deployment">
      <h3 class="message">Deploy</h3>
      {stories("deploy")}
    </div>
  </body>
  </html>
  
  private def stories(phase: String) = 
    for(story <- Story.findAllByPhase(phase)) yield 
      <div id={story.number} class="story">
        <fieldset>
          <legend>{story.number}</legend>
          <div class="section">
            <label>{story.title}</label>
          </div>
        </fieldset>
      </div>
  
  
  private def header = 
    <head>
  	  <meta charset="UTF-8" />
  	  <title>weKanban: A simple Kanban board</title>
  	  <script type="text/javascript" src="/js/jquery-1.4.2.js"/>
  	  <script type="text/javascript" src="/js/jquery.ui.core.js"/>
  	  <script type="text/javascript" src="/js/jquery.ui.widget.js"/>
  	  <script type="text/javascript" src="/js/jquery.ui.mouse.js"/>
  	  <script type="text/javascript" src="/js/jquery.ui.draggable.js"/>
  	  <script type="text/javascript" src="/js/jquery.ui.droppable.js"/>
  	  <script type="text/javascript" src="/js/main.js"/>
  	  <link type="text/css" href="/css/main.css" rel="stylesheet" />
  	  <script type="text/javascript">
        init()
  	  </script>
  </head>  
}