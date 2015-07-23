package com.e1.pdj.js;

import org.mozilla.javascript.*;

import com.cycling74.max.*;

public class JSMaxObject extends ScriptableObject {
	private static final long serialVersionUID = 1L;
	static String[] API_EXPOSED = { "post", "error", "outlet", "outletBang", "messnamed"};
	
	public String getClassName() {
		return "JSMaxObject";
	}
	public static void post(Context cx, Scriptable thisObj,
            Object[] args, Function funObjl) {
		StringBuffer out = new StringBuffer();
		
		for (int i=0; i<args.length;i++) {
			out.append(Context.toString(args[i]));
			out.append(' ');
		}
		
		MaxObject.post(out.toString());
	}
	
	public static void error(Context cx, Scriptable thisObj,
            Object[] args, Function funObjl) {
		StringBuffer out = new StringBuffer();
		
		for (int i=0; i<args.length;i++) {
			out.append(Context.toString(args[i]));
			out.append(' ');
		}
		
		MaxObject.error(out.toString());
	}	
	
	public static void outlet(Context cx, Scriptable thisObj,
            Object[] args, Function funObjl) {
		int id = (int) Context.toNumber(args[0]);
		int startIdx;
		
		if ( args[1] instanceof Object[] ) {
			args = (Object []) args[1];
			startIdx = 0;
		} else {
			startIdx = 1;
		}
		
		Atom atoms[] = new Atom[args.length-startIdx];
		
		for (int i=startIdx; i<args.length; i++) {
			if ( args[i] instanceof Number ) {
				Number num = (Number) args[i];
				atoms[i - startIdx] = Atom.newAtom(num.floatValue());
			} else if ( args[i] instanceof Boolean ) {
				Boolean bool = (Boolean) args[i];
				atoms[i - startIdx] = Atom.newAtom(bool.booleanValue() ? "1" : "0");				
			} else {
				atoms[i - startIdx] = Atom.newAtom(Context.toString(args[i]));
			}
		}
		
		MaxObject obj = (MaxObject) thisObj.get("__maxObject", thisObj);
		
		obj.outlet(id, atoms);
	}
	
	public static void messnamed(Context cx, Scriptable thisObj,
            Object[] args, Function funObjl) {
		if ( args.length < 2 )
			throw new JJSRuntimeException("Missing arugment to messnamed");
		
		String dest = Context.toString(args[0]);
		String msg = Context.toString(args[1]);
		
		if ( args.length == 2 ) {
			MaxSystem.sendMessageToBoundObject(dest, msg, null);
			return;
		}
		
		int startIdx;
		
		if ( args[2] instanceof Object[] ) {
			args = (Object []) args[2];
			startIdx = 0;
		} else {
			startIdx = 2;
		}
		
		Atom atoms[] = new Atom[args.length-startIdx];
		
		for (int i=startIdx; i<args.length; i++) {
			if ( args[i] instanceof Number ) {
				Number num = (Number) args[i];
				atoms[i - startIdx] = Atom.newAtom(num.floatValue());
			} else if ( args[i] instanceof Boolean ) {
				Boolean bool = (Boolean) args[i];
				atoms[i - startIdx] = Atom.newAtom(bool.booleanValue() ? "1" : "0");				
			} else {
				atoms[i - startIdx] = Atom.newAtom(Context.toString(args[i]));
			}
		}
		
		MaxSystem.sendMessageToBoundObject(dest, msg, atoms);
	}
	
	public static void outletBang(Context cx, Scriptable thisObj,
            Object[] args, Function funObjl) {
		int id = (int) Context.toNumber(args[0]);
		MaxObject obj = (MaxObject) thisObj.get("__maxObject", thisObj);
		obj.outletBang(id);
	}
	
}
