/*
	void create_AST();
	void commit_file();
	void commit_file_from_int(int);
	-----
	{
		a: Trie(null, {
			a: Trie(null, {
				a: Trie(def_aaa, {})
				b: Trie(def_aab, {})
			})
			b: Trie(null, {
				b: Trie(def_abb, {})
			})
		})
	}
	
	def x = 3 + 5
	def f() {
		def y = x
	}
 */

import java.util.*;

record Definition(String name, double completionValue) implements Completable {
}

public class Main {
	private static String abbreviate(String original) {
		boolean split = true;
		var abbr = new StringBuilder();
		for (char c : original.toCharArray()) {
			if (c == '_' || Character.isUpperCase(c))
				split = true;
			if (c != '_' && split) {
				abbr.append(Character.toUpperCase(c));
				split = false;
			}
		}
		return abbr.toString();
	}
	
	public static void main(String[] args) {
		var scanner = new Scanner(System.in);
		
		var scope = new CompletionScope<Definition>();
		
		for (;;) {
			System.out.println("1. Add definition");
			System.out.println("2. Remove definitions for name");
			System.out.println("3. Autocomplete");
			System.out.println("4. Exit");
			switch (scanner.nextInt()) {
				case 1 -> {
					System.out.print("Name: ");
					String name = scanner.next();
					System.out.print("Value: ");
					double value = scanner.nextDouble();
					
					var def = new Definition(name, value);
					
					scope.put(name, def);
					var abbr = abbreviate(name);
					if (!abbr.equals(name))
						scope.put(abbr, def);
				}
				case 2 -> {
					System.out.print("Name: ");
					String name = scanner.next();
					scope.removeAll(name);
				}
				case 3 -> {
					System.out.print("Prefix: ");
					String prefix = scanner.next();
					
					var completions = scope.getCompletions(prefix);
					
					if (completions.isEmpty()) {
						System.out.println("NO COMPLETIONS");
					} else {
						System.out.println("Completions:");
						for (var completion : completions)
							System.out.println("\t" + completion.name() + ", value = " + completion.completionValue());
					}
				}
				case 4 -> {
					return;
				}
				default -> System.out.println("Invalid choice!");
			}
		}
		/*var defs = List.of(
			new Definition("control_file", 0.5),
			new Definition("commit_file", 1.0),
			new Definition("ConfigFile", 10),
			new Definition("CF", 10)
		);
		for (var def : defs) {
			ctx.put(def.name(), def);
			var abbr = abbreviate(def.name());
			if (!abbr.equals(def.name()))
				ctx.put(abbr, def);
		}
		
		System.out.print("enter completion prefix: ");
		var prefix = scanner.nextLine();*/
		
	}
}
