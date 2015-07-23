package com.e1.pdj.js;

import java.io.*;

import com.cycling74.max.*;

import org.mozilla.javascript.*;

/**
 * JavaJavaScript (jjs) object is using Rhino to offer JavaScript scripting capabilities. 
 */
public class JavaJavaScript extends MaxObject {
	private Object jsArgs[] = null; 
	private String scriptFullPath;
	private String scriptName;
	private int numOutlets = 0;
	private int numInlets = 0;
	Scriptable scope;
	
	static Scriptable globalObject;
	
    public JavaJavaScript() {
        throw new JJSRuntimeException("missing script name");
    }

	public JavaJavaScript(Atom args[]) {
		if (args.length < 1) {
			throw new JJSRuntimeException("missing script name");
		}

		scriptName = args[0].toString();

		scriptFullPath = MaxSystem.locateFile(scriptName);
		if (scriptFullPath == null) {
			if (!new File(scriptName).exists()) {
				throw new JJSRuntimeException("script not found: " + scriptName);
			}
			scriptFullPath = scriptName;
		} else {
			scriptFullPath = new File(scriptFullPath, scriptName).toString();
		}

		jsArgs = atoms2js(args);

		reload();

		Object x = scope.get("inlets", scope);
		if (x != Scriptable.NOT_FOUND) {
			numInlets = (int) Context.toNumber(x);
		} else {
			numInlets = 1;
		}

		x = scope.get("outlets", scope);
		if (x != Scriptable.NOT_FOUND) {
			numOutlets = (int) Context.toNumber(x);
		} else {
			numOutlets = 1;
		}
		declareIO(numInlets, numOutlets);
	}

    private Object[] atoms2js(Atom[] args) {
    	Object ret[] = new Object[args.length];
    	
    	for(int i=0;i<args.length;i++) {
    		if ( args[i].isString() ) {
    			ret[i] = args[i].toString();
    		}
    		if ( args[i].isFloat() ) {
    			ret[i] = new Float(args[i].toFloat());
    		}
    		if ( args[i].isInt() ) {
    			ret[i] = new Integer(args[i].toInt());
    		}
    	}
		return ret;
    }

    public void reload() {
        Context cx = Context.enter();
        try {
            scope = cx.initStandardObjects(methodProxy);
            Scriptable arguments = cx.newArray(scope, jsArgs);
            scope.put("jsarguments", scope, arguments);
            scope.put("global", scope, globalObject);
            scope.put("__maxObject", scope, this);
            
            Reader in = new FileReader(scriptFullPath);
            cx.evaluateReader(scope, in, scriptName, 1, null);
            in.close();            
        } catch (Exception e) {
        	throw new JJSRuntimeException(e);
        } finally {
            Context.exit();        
        }
    } 
    
    public void inlet(float val) {
        Context cx = Context.enter();
		try {
			Object fObj = scope.get("float_msg", scope);
			if (!(fObj instanceof Function)) {
				anything("float", new Atom[] { Atom.newAtom(val) });
				return;
			} else {
				Object functionArgs[] = { new Float(val) };
				Function f = (Function) fObj;
				ScriptableObject.defineProperty(scope, "inlet", new Integer(getInlet()), ScriptableObject.DONTENUM);
				f.call(cx, scope, scope, functionArgs);
			}
		} finally {
			Context.exit();
		}
    }
 
    public void inlet(int val) {
    	inlet((float) val);
    }
    
	protected void anything(String name, Atom args[]) {
		Context cx = Context.enter();
		try {
			Object fObj = scope.get(name, scope);
			ScriptableObject.defineProperty(scope, "inlet", new Integer(getInlet()), ScriptableObject.DONTENUM);
			ScriptableObject.defineProperty(scope, "messagename", name, ScriptableObject.DONTENUM);
			Object objArgs[] = null;
			
			if ( args != null ) {
				objArgs = atoms2js(args);
				ScriptableObject.defineProperty(scope, "arguments", objArgs, ScriptableObject.DONTENUM);
			}
			
			if (!(fObj instanceof Function)) {
				fObj = scope.get("anything", scope);
				if (!(fObj instanceof Function)) {
					error("jjs: don't know what to do with: " + name);
				} else {
					Function f = (Function) fObj;
					f.call(cx, scope, scope, objArgs);					
				}
			} else {
				Function f = (Function) fObj;
				f.call(cx, scope, scope, objArgs);
			}
		} finally {
			Context.exit();
		}
	}
	
	protected void loadbang() {
		Context cx = Context.enter();
		try {
			Object fObj = scope.get("loadbang", scope);
			if ((fObj instanceof Function)) {
				Function f = (Function) fObj;
				f.call(cx, scope, scope, null);
			}
		} finally {
			Context.exit();
		}
	}
	
	
	static JSMaxObject methodProxy;
	static {
        Context cx = Context.enter();
    	methodProxy = new JSMaxObject();
        methodProxy.defineFunctionProperties(JSMaxObject.API_EXPOSED, JSMaxObject.class,
                ScriptableObject.DONTENUM);
        try {
            globalObject = cx.initStandardObjects();
        } finally {
            Context.exit();        
        }
	}
	
}