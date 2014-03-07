package com.futureplatforms.kirin.gwt.compile;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public abstract class InterfaceGenerator {
	protected String partialPath;
	protected JClassType jsObjType;
	
	public InterfaceGenerator(String partialPath) {
		this.partialPath = partialPath;
	}
	
	public abstract String preambleForClass(String name);
	public abstract String postamble();
	public abstract String getMethodSignature(JMethod method);
	public abstract String filenameExtension();
	
	public final void generateProtocolResource(TreeLogger logger,
			GeneratorContext context, JClassType nativeObjectType)
			throws UnableToCompleteException {
	    TypeOracle oracle = context.getTypeOracle();
		jsObjType = oracle.findType(JavaScriptObject.class.getName());
		final OutputStream outStream = context.tryCreateResource(logger, partialPath + nativeObjectType.getSimpleSourceName() + filenameExtension());
		
		if(outStream == null) {
		    return; // We've already generated this protocol
		}
		// TODO Auto-generated method stub
		PrintWriter printWriter = new PrintWriter(outStream);
		try {
			printWriter.format(preambleForClass(nativeObjectType.getSimpleSourceName()));
			while (nativeObjectType != null) {
				for (JMethod method : nativeObjectType.getMethods()) {
					if (!method.isPublic() || method.isStatic()) {
						continue;
					}
					
					if (!method.getReturnType().getSimpleSourceName().equals("void")) {
						continue;
					}
					
					if (method.isAnnotationPresent(NoBind.class)) {
					    continue;
					}
					
					// TODO filter out the lifecycle methods.
					if (isKirinModuleMethod(method)) {
						continue;
					}
					
					// TODO check if the method is exported or not.
					if (!isMethodExported(method)) {
						continue;
					}
					
					if (!parameterNamesValid(logger, method)) {
						logger.log(Type.ERROR, "Could not process " + nativeObjectType.getName() + " :: " + method.getName());
						throw new UnableToCompleteException();
					}
					
					String methodSig = getMethodSignature(method);
					if (methodSig != null) {
						printWriter.append(methodSig).append('\n');
					}
				}
				nativeObjectType = nativeObjectType.getSuperclass();
			}
			printWriter.format(postamble());
		} finally {
			printWriter.flush();
			printWriter.close();
		}
		context.commitResource(logger, outStream);
	}

	private boolean isMethodExported(JMethod method) {
		return method.getAnnotation(NoExport.class) == null;
	}
	
	private boolean isKirinModuleMethod(JMethod method) {
		String name = method.getName();
		
		String[] kirinMethods = {"onLoad", "onResume", "onPause", "onUnload"};
		
		for (String kirinMethod : kirinMethods) {
			if (kirinMethod.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean parameterNamesValid(TreeLogger logger, JMethod method) {
		// These are keywords which exist in C#, Obj-C or Javascript, but not in Java
		String[] keywords = { "id", "params", "ref", "arguments", "var", "foreach" };
		JParameter[] params = method.getParameters();
		for (JParameter param : params) {
			for (String keyword : keywords) {
				if (keyword.equals(param.getName())) {
					logger.log(Type.ERROR, "Parameter " + param + " clashes with a native keyword, choose a different name!");
					return false;
				}
			}
		}
		return true;
	}
}
