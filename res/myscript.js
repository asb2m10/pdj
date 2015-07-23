post("js in pdj")

inlets = 2; // number of inlets to defined
outlets = 4; // number of outlets to define

// this function will be called when a float is sended an outlet
function float_msg(val) {
	outlet(0, val)
}

// this function is called when the message "what" is sended. 
function what(msg1, msg2) {
	post("what ?", msg1, msg2);
	post("number of arguments", arguments.length);
}

// send float '1.0' to [r reader] object.
function great() {
	messnamed("reader", "float", 1);
}
