function moveCard(storyNumber, phase) {
  $.post("/card/move", {storyNumber: storyNumber, phase: phase}, function(message) {
    $('#message').html(message)          
  });  
}

function init() {
  $(function() {
    $(".story").each(function() {
      $(this).draggable();
    });
    $("#readyPhase").droppable({
      drop: function(event, ui) { moveCard(ui.draggable.attr("id"), "ready") }
    });
    $("#devPhase").droppable({
      drop: function(event, ui) { moveCard(ui.draggable.attr("id"), "dev") }
    });
    $("#testPhase").droppable({
      drop: function(event, ui) { moveCard(ui.draggable.attr("id"), "test") }
    });
    $("#deployPhase").droppable({
      drop: function(event, ui) { moveCard(ui.draggable.attr("id"), "deploy") }
    });
  });
}