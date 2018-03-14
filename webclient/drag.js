function setButtonDraggable(buttonElement, on) {
    if (on) {
        $(buttonElement).draggable({
            cancel: false
        })
    }
    $(buttonElement).draggable({
        cancel: true
    })
}
