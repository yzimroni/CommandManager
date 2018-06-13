package net.yzimroni.commandmanager.command.methodexecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.yzimroni.commandmanager.command.Command;
import net.yzimroni.commandmanager.command.CommandExecutor;
import net.yzimroni.commandmanager.command.args.ArgumentData;

import org.bukkit.command.CommandSender;

@Deprecated
public class MethodExecutor implements CommandExecutor {
	
	private MethodExecutorClass object;
	private Method method;
	
	public static MethodExecutor createByMethodName(MethodExecutorClass object, String name) {
		try {
			Method method = object.getClass().getMethod(name, CommandSender.class, Command.class, ArgumentData.class);
			if (validateMethod(object, method, "name '" + name + "'")) {
				return new MethodExecutor(object, method);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static MethodExecutor createByMethodId(MethodExecutorClass object, String id) {
		try {
			Method method = null;
			for (Method m : object.getClass().getMethods()) {
				if (m.isAnnotationPresent(MethodId.class)) {
					MethodId methodId = m.getAnnotation(MethodId.class);
					if (methodId != null) {
						if (methodId.value().equals(id)) {
							method = m;
							break;
						}
					}
				}
			}
			if (validateMethod(object, method, "id '" + id + "'")) {
				return new MethodExecutor(object, method);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static boolean validateMethod(MethodExecutorClass object, Method method, String name) throws Exception {
		if (method != null) {
			if (method.getReturnType().equals(void.class)) {
				Class<?>[] parameters = method.getParameterTypes();
				if (parameters.length == 3) {
					if (parameters[0].equals(CommandSender.class) && parameters[1].equals(Command.class) && parameters[2].equals(ArgumentData.class)) {
						return true;
					} else {
						throw new IllegalArgumentException("Method " + method.getName() + " in class '" + object.getClass().getCanonicalName() + "' have invalid parameters");
					}
				} else {
					throw new IllegalArgumentException("Method " + method.getName() + " in class '" + object.getClass().getCanonicalName() + "' have invalid parameters number");
				}
			} else {
				throw new IllegalArgumentException("Method " + method.getName() + " in class '" + object.getClass().getCanonicalName() + "' have invalid return type");
			}
		} else {
			throw new NoSuchMethodException("Method " + name + " not found in class '" + object.getClass().getCanonicalName() + "'");
		}
		
	}
	
	
	private MethodExecutor(MethodExecutorClass object, Method method) {
		this.object = object;
		this.method = method;
	}
	
	@Override
	public void executeCommand(CommandSender sender, Command command, ArgumentData args) {
		try {
			method.invoke(object, sender, command, args);
		} catch (InvocationTargetException e) {
			e.getTargetException().printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MethodExecutorClass getObject() {
		return object;
	}

	public Method getMethod() {
		return method;
	}

}
