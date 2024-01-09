/*
	void create_AST();
	void commit_file();
	void commit_file_from_int(int);
	
	int f() {
		control_file();
		{
			int cons;
		}
	}
	
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

import java.util.Scanner;

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
		System.out.println(abbr);
		return abbr.toString();
	}
	
	public static void main(String[] args) {
		var ctx = new CompletionContext<Definition>();
		
		var def1 = new Definition("control_file", 0.5);
		ctx.put(def1.name(), def1);
		ctx.put(abbreviate(def1.name()), def1);
		
		var def2 = new Definition("commit_file", 1.0);
		ctx.put(def2.name(), def2);
		ctx.put(abbreviate(def2.name()), def2);
		
		var def3 = new Definition("ConfigFile", 10);
		ctx.put(def3.name(), def3);
		ctx.put(abbreviate(def3.name()), def3);
		
		System.out.print("enter completion prefix: ");
		var prefix = new Scanner(System.in).nextLine();
		var completions = ctx.getCompletions(prefix);
		
		
		System.out.println("COMPLETIONS:");
		for (var completion : completions)
			System.out.println(completion.name());
	}
}
