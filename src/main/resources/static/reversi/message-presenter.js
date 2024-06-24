class MessagePresenter {
    #messageArray = [];
    _process(messageArray) {
        throw new Error("not implemented");
    }
    pushMessage(message) {
        this.#messageArray.push(message);
    }
    process() {
        this._process(this.#messageArray);
        this.#messageArray = [];
    }
}

class TextareaDomMessagePresenter extends MessagePresenter {
    #textarea = null;
    constructor(textareaElement) {
        super();
        this.#textarea = textareaElement;
    }
    _process (messageArray) {
        if (messageArray.length ===0) return;
        var copied =messageArray.slice();
        while(copied.length>0){
            var presentedMessages =this.#textarea.value.split("\n");
            while(presentedMessages.length > 100) {
                presentedMessages.shift();
            }
            presentedMessages.push(copied.shift());
        }
        this.#textarea.value = presentedMessages.join("\n");
        this.#textarea.scrollTo(0, this.#textarea.scrollHeight);
    }
}

export {TextareaDomMessagePresenter};