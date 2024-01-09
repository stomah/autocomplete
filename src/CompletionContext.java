import org.jetbrains.annotations.Nullable;

import java.util.*;

class Trie<Word> {
	ArrayList<Word> words = new ArrayList<>();
	HashMap<Character, Trie<Word>> children = new HashMap<>();
	
	@Nullable Trie<Word> getChild(char firstChar) {
		return children.get(firstChar);
	}
	
	@Nullable Trie<Word> getChildren(String prefix) {
		var result = this;
		for (int i = 0; i < prefix.length() && result != null; i++)
			result = result.getChild(prefix.charAt(i));
		return result;
	}
	
	Trie<Word> getForceChild(char firstChar) {
		var c = children.get(firstChar);
		if (c == null) {
			c = new Trie<>();
			children.put(firstChar, c);
		}
		return c;
	}
	
	Trie<Word> getForceChildren(String prefix) {
		var result = this;
		for (char c : prefix.toCharArray())
			result = result.getForceChild(c);
		return result;
	}
}

public class CompletionContext<Definition extends Completable> {
	private final Trie<Definition> trie = new Trie<>();
	
	public void put(String name, Definition def) {
		trie.getForceChildren(name).words.add(def);
	}
	
	/*public void remove(String name, Definition def) {
		var t = trie.getChildren(name);
		if (t != null)
			t.words.remove(def);
	}*/
	
	public List<Definition> getCompletions(String prefix) {
		var completions = new ArrayList<Definition>();
		
		var startingTrie = trie.getChildren(prefix);
		if (startingTrie == null)
			return completions;
		
		int fullMatchCount = startingTrie.words.size();
		
		var pendingTries = new ArrayDeque<Trie<Definition>>();
		pendingTries.push(startingTrie);
		
		while (!pendingTries.isEmpty()) {
			var nextTrie = pendingTries.pop();
			completions.addAll(nextTrie.words);
			pendingTries.addAll(nextTrie.children.values());
		}
		
		completions
			.subList(0, fullMatchCount)
			.sort(Comparator.comparingDouble(d -> -d.completionValue()));
		completions
			.subList(fullMatchCount, completions.size())
			.sort(Comparator.comparingDouble(d -> -d.completionValue()));
		return completions;
	}
}
