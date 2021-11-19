package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	
		
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		root = builder();
		
	}
	private TagNode builder() {
		
		String text = null;
		boolean reader = sc.hasNextLine();
		int length;
		
		if (reader == true)
			text = sc.nextLine();
		else
			return null; 
		
		length = text.length();
		boolean x = false;
		
		if (text.charAt(0) == '<'){		
			text = text.substring(1, length - 1);
			if (text.charAt(0) == '/')				
				return null;
			else {
				x = true; 
			}
		}
		TagNode temp = new TagNode (text, null, null);
		if(x == true)
			temp.firstChild = builder();											
		temp.sibling = builder();												
		return temp;
	}
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		replacer(root, oldTag, newTag);
	}
	private void replacer(TagNode temp, String oldTag, String newTag) {
		TagNode root = temp;

		if (root == null)
			return;
		
		if (root.tag.equals(oldTag)) {
			root.tag = newTag;
		}
		replacer(temp.firstChild, oldTag, newTag);
		replacer(temp.sibling, oldTag, newTag);
	}
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		TagNode ptr = new TagNode(null, null, null);										
		TagNode temp;
		
		ptr = rowbolder(root);
		if (ptr == null) {
			System.out.println("No table found");
			return;
		}
		ptr = ptr.firstChild;
		for(int i = 1; i < row; i++) {
			ptr = ptr.sibling;
		} 
		
		for(temp = ptr.firstChild; temp != null; temp = temp.sibling) {
			temp.firstChild = new TagNode("b", temp.firstChild, null);
			
		}

	} 
	
	private TagNode rowbolder(TagNode root) { 
		if (root == null)
			return null; 
		
		TagNode tag = null;
		String str = root.tag;
		
		if(str.equals("table")) { 
			tag = root; 
			return tag;
		} 
		if(tag == null) { 
			tag = rowbolder(root.sibling);
		} 
		if(tag == null) {
			tag = rowbolder(root.firstChild);
		}
		return tag;
	}
	
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		root=this.tagDeleter(root, tag);
		
	}
	
	private TagNode tagDeleter(TagNode root, String r){
		if (root==null)
			return null;
		String token=root.tag;
		root.firstChild=this.tagDeleter(root.firstChild,r);
		if((token.equals("p")&&token.equals(r))||(token.equals("em")&&token.equals(r))||(token.equals("b")&&token.equals(r))){
			this.LastTag(root.firstChild).sibling=root.sibling;
			root=root.firstChild;
		}
		else if((token.equals("ol")&&token.equals(r))||(token.equals("ul")&&token.equals(r))){
			TagNode child=root.firstChild;
			while(child!=null){
				if(child.tag.equals("li"))
					child.tag="p";
				child=child.sibling;
			}
			child=root.firstChild;
			this.LastTag(child).sibling=root.sibling;
			root=root.firstChild;
		}
		root.sibling=this.tagDeleter(root.sibling, r);
		return root;
	}

	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		ArrayList<String> array = new ArrayList<String>();
		array.add(".");
		array.add(":");
		array.add(",");
		array.add("!");
		array.add("?");
		array.add(";");
		root=this.TagAdder(root, word, tag, array);
		
	}
	private TagNode addLastTag(TagNode t, String tag){
		if(t==null){
			return new TagNode(tag,null,null);			
		}
		TagNode temp=t;
		while(temp.sibling!=null)
			temp=temp.sibling;
		temp.sibling=new TagNode(tag,null,null);
		return t;
	}
	
	private TagNode LastTag(TagNode t){
		if(t==null)
			return t;
		while(t.sibling!=null)
			t=t.sibling;
		return t;
	}
	
	private TagNode TagAdder(TagNode root, String word, String tag, ArrayList<String> a){
		if(root==null) {
			return null;
		}
		root.firstChild=this.TagAdder(root.firstChild, word, tag, a);
		root.sibling=this.TagAdder(root.sibling, word, tag, a);
		
		String temp="",ans="";
		String text=root.tag+" ";
		TagNode newTag=null;
		while(text.length()!=0){
			int index=text.indexOf(" ");
			temp=text.substring(0, index).toLowerCase();
			if(temp.contains(word)==false)
				ans+=text.substring(0,index)+ " ";
			
			if(temp.contains(word)&&temp.length()<=word.length()+1){
				if(temp.length()==word.length()+1 && a.contains(temp.substring(temp.length()-1))){
					if(ans.length()!=0)
						newTag=this.addLastTag(newTag, ans);
					newTag=this.addLastTag(newTag, tag);
					this.LastTag(newTag).firstChild= new TagNode(text.substring(0, index),null,null);
					ans="";
				}
				else if (temp.length()==word.length()){
					if(ans.length()!=0)
						newTag=this.addLastTag(newTag, ans);
					newTag=this.addLastTag(newTag, tag);
					this.LastTag(newTag).firstChild= new TagNode(text.substring(0, index),null,null);
					ans="";
				}
			}
			
			text=text.substring(index+1);
			
		}
		if(newTag==null)
			return root;
		this.LastTag(newTag).sibling=root.sibling;
		root=newTag;
		
		return root;
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
