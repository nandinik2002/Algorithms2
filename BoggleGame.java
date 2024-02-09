import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;


public class BoggleGame implements BoggleGameInterface{

    @Override
    public char[][] generateBoggleBoard(int size) {
        if(size <= 0){
            return null;
        }
        int stringLength = size*size;
        if(stringLength <= 0){
            return null;
        }
        String s = generateRandomString(stringLength);
        char[][] board = new char[size][size];
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                board[i][j] = s.charAt(i*size+j);
            }
        }
        return board;
    }

    @Override
    public int countWords(char[][] boggleBoard, DictInterface dictionary) {
      HashSet<String> foundWords = new HashSet<>(); //stores found words so words are not repeated in the count
      boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[0].length]; //tracks the visited cells
      //this nested loop iterates over each cell
      for (int i = 0; i < boggleBoard.length; i++) {
          for (int j = 0; j < boggleBoard[0].length; j++) {
              dfsCountWords(i, j, boggleBoard, dictionary, new StringBuilder(), visited, foundWords); //helper method that uses depth first search to go through the board
          }
      }
      return foundWords.size(); //returns the number of unique words found in the boggle board
}




private void dfsCountWords(int i, int j, char[][] board, DictInterface dict, StringBuilder word, boolean[][] visited, HashSet<String> foundWords) {
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j]) return; //ends method if we're out of bounds or have already visited the cell
    word.append(Character.toLowerCase(board[i][j])); //adds the character of the current cell to the word
    visited[i][j] = true; //marks the current cell as visited
    int result = dict.searchPrefix(new StringBuilder(word));
    if ((result == 2 || result == 3)) {
        foundWords.add(word.toString()); //if the word is in the dictionary we add it to the foundWords HashSet
    }
    if (result == 1 || result == 3) { //condition is if the current word is a prefix or valid word continue into the body of the condition
        for (int di = -1; di <= 1; di++) { //continues into other cells
            for (int dj = -1; dj <= 1; dj++) {
                if (di != 0 || dj != 0) {
                    dfsCountWords(i + di, j + dj, board, dict, word, visited, foundWords);
                }
            }
        }
    }

    word.setLength(word.length() - 1); //backtracks by removing the last character
    visited[i][j] = false; //marks the cell as not visited
}






    @Override
    public int countWordsOfCertainLength(char[][] boggleBoard, DictInterface dictionary, int wordLength) {
      int count = 0; //tracks the number of words found
    boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[0].length]; //tracks the visited cells
    StringBuilder word = new StringBuilder(); //used to build the words

    for (int i = 0; i < boggleBoard.length; i++) {  //nested loops used to iterate over every cell in the boggle board
        for (int j = 0; j < boggleBoard[i].length; j++) {
            count += dfsCountWordsOfCertainLength(i, j, boggleBoard, dictionary, word, visited, 0, wordLength);
        }
    }
    return count; //returns count of found words of the specified length
 }




    private int dfsCountWordsOfCertainLength(int i, int j, char[][] board, DictInterface dict, StringBuilder word, boolean[][] visited, int currentLength, int wordLength) {
    if (currentLength == wordLength) { //if the found word is the same length the user is looking for
        if (dict.searchPrefix(new StringBuilder(word.toString().toLowerCase())) == 2) return 1; //returns 1 if the word exists and 0 if it doesnt
        return 0;
    }
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j]) return 0;

    visited[i][j] = true; //marks the current cell as visited
    word.append(Character.toLowerCase(board[i][j])); //add the current cells letter to the word
    int count = 0; //tracks the number of words found
    currentLength++;
    if (currentLength <= wordLength) { //condition is if the current length is less than or equal to the desired length
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}}; //different directions to explore
        for (int[] d : directions) {
          //explores surrounding cells
            count += dfsCountWordsOfCertainLength(i + d[0], j + d[1], board, dict, word, visited, currentLength, wordLength);
        }
    }
    word.setLength(word.length() - 1); //backtracking by removing the last character, then marking the current cell as not visited and then finally returns count which is the total count of the words found
    visited[i][j] = false;
    return count;
}




    @Override
    public boolean isWordInDictionary(DictInterface dictionary, String word) {
      return dictionary.searchPrefix(new StringBuilder(word)) == 2; //returns if the word is in the dictionary
    }




    @Override
    public boolean isWordInBoard(char[][] boggleBoard, String word) {
      word = word.toUpperCase(); //converts word to uppercase since in the boggle board the characters are all in uppercase
      for(int i = 0; i < boggleBoard.length; i++) { //goes through the entire boggle board
          for(int j = 0; j < boggleBoard[0].length; j++) {
              if(checkWordHelper(boggleBoard, i, j, word, 0))
                  return true;
          }
      }
      return false; //returns false if the word isnt found in the board
    }





    private boolean checkWordHelper(char[][] boggleBoard, int row, int col, String word, int index) {
      //this is the base case: if the index reaches the length of the word that means the word has been found and returns true
        if(index == word.length())
            return true;
        if(row < 0 || col < 0 || row >= boggleBoard.length || col >= boggleBoard[0].length || boggleBoard[row][col] != word.charAt(index)) //returns false if we are out of bounds or character doesnt match
            return false;

        char temp = boggleBoard[row][col]; //stores the current character in a temp variable
        boggleBoard[row][col] = '#'; //marks the current cell as visited
        //boolean found recursively checks every cell of the board
        boolean found = checkWordHelper(boggleBoard, row + 1, col, word, index + 1) ||
                        checkWordHelper(boggleBoard, row - 1, col, word, index + 1) ||
                        checkWordHelper(boggleBoard, row, col + 1, word, index + 1) ||
                        checkWordHelper(boggleBoard, row, col - 1, word, index + 1) ||
                        checkWordHelper(boggleBoard, row + 1, col + 1, word, index + 1) ||
                        checkWordHelper(boggleBoard, row + 1, col - 1, word, index + 1) ||
                        checkWordHelper(boggleBoard, row - 1, col + 1, word, index + 1) ||
                        checkWordHelper(boggleBoard, row - 1, col - 1, word, index + 1);

        boggleBoard[row][col] = temp; //restores the original character

        return found; //returns if the word is found
    }





    @Override
    public String anyWord(char[][] boggleBoard, DictInterface dictionary) {
      boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[0].length];
     StringBuilder word = new StringBuilder();
     //nested loops iterate through every cell in the board
     for (int i = 0; i < boggleBoard.length; i++) {
         for (int j = 0; j < boggleBoard[0].length; j++) {
             if (dfsFindAnyWord(i, j, boggleBoard, dictionary, word, visited)) { //uses the dfs method from the current cell to find any valid words
                 return word.toString(); //returns found word
             }
         }
     }
     return null; //returns null if theres no valid word found in the board
  }





  private boolean dfsFindAnyWord(int i, int j, char[][] board, DictInterface dict, StringBuilder word, boolean[][] visited) {
    if (dict.searchPrefix(word) == 2 || dict.searchPrefix(word) == 3) { //checks if the current word is a valid word or prefix in the dictionary adnd returns true if it is valid
        return true;
    }
    //returns false if the position is out of bounds or already visited: base case
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j]) return false;

    visited[i][j] = true;
    word.append(board[i][j]); //adds the current cells character to the word
    int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}}; //stores different directions to explore
    for (int[] d : directions) { //loop to go through all cells
        int newRow = i + d[0];
        int newCol = j + d[1];
        if (dfsFindAnyWord(newRow, newCol, board, dict, word, visited)) { //recursively searchs all cells
            return true; //returns true if a valid word is found
        }
    }

    word.deleteCharAt(word.length() - 1); //removes last chatacter
    visited[i][j] = false; //marks current position as not visited
    return false;
}







    @Override
    public ArrayList<Tile> markWordInBoard(char[][] boggleBoard, String word) {
      if (word == null || word.isEmpty()) { //returns empty list if the word is null or empty
          //System.out.println("No word provided or word is empty."); //used for debugging
          return new ArrayList<>();
      }

      for (int i = 0; i < boggleBoard.length; i++) { //nested loops to go through each cell
          for (int j = 0; j < boggleBoard[i].length; j++) {
              if (boggleBoard[i][j] == word.charAt(0)) { //searches for word from the position of its first letter
                  ArrayList<Tile> path = new ArrayList<>();
                  boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[0].length];
                  if (dfsMarkWordInBoard(i, j, boggleBoard, word, 0, visited, path)) { //uses dfs to find the word in the boggle board
                      return path;
                  }
              }
          }
      }
      return new ArrayList<>(); //returns empty list if the word isnt found
  }






    private boolean dfsMarkWordInBoard(int i, int j, char[][] board, String word, int index, boolean[][] visited, ArrayList<Tile> path) {
    if (index == word.length()) { //this is a base case: returns true if the word is found
        return true;
    }
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) { //this is a base case too: returns false if the current position is out of bounds
        return false;
    }
    if (visited[i][j] || board[i][j] != word.charAt(index)) { //another base case that returns false if the current position has already been visited or doesnt match the current character of the word
        return false;
    }

    visited[i][j] = true; //marks current position as visited
    path.add(new Tile(i, j)); //adds the current position to the path
    int[][] dirs = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}}; //stores the possible directions to go in
    for (int[] d : dirs) { //recursively looks through each cell
        if (dfsMarkWordInBoard(i + d[0], j + d[1], board, word, index + 1, visited, path)) {
            return true;
        }
    }
    path.remove(path.size() - 1); //removes the last added position from the path
    visited[i][j] = false; //remarks the current position as not visited
    return false;
}





    @Override
    public boolean checkTiles(char[][] boggleBoard, ArrayList<Tile> tiles, String word) {
      if (tiles.size() != word.length()) return false; //checks if the number of tiles matches the length of the word
      for (int i = 0; i < tiles.size(); i++) { //goes through each tile to confirm if its corresponding character matches the word
          Tile tile = tiles.get(i);
          if (boggleBoard[tile.row][tile.col] != word.charAt(i)) { //condition to check if the character at the tile's position matches its corresponding character matches the word and returns true if not
              return false;
          }
          if (i > 0 && !isAdjacent(tiles.get(i - 1), tile)) { //starting from the 2nd tile, this checks adjacency between consecutive tiles
              return false;
          }
      }
      return true; //returns true if all the tiles form the word and are adjacent
    }

    private boolean isAdjacent(Tile prevTile, Tile currTile) { //checks if 2 tiles are adjacent on the board
    int rowDiff = Math.abs(prevTile.row - currTile.row);
    int colDiff = Math.abs(prevTile.col - currTile.col);
    return rowDiff <= 1 && colDiff <= 1 && !(rowDiff == 0 && colDiff == 0);
}





    @Override
    public String anyWord(char[][] boggleBoard, DictInterface dictionary, int length) {
      boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[0].length]; //array to keep track of the already visited cells
     for (int i = 0; i < boggleBoard.length; i++) { //nested loops to go through every cell of the board
         for (int j = 0; j < boggleBoard[i].length; j++) {
             StringBuilder word = new StringBuilder();
             if (dfsFindAnyWordOfLength(i, j, boggleBoard, dictionary, word, visited, length)) { //tries to find word of the required length starting from the current cell
                 return word.toString(); //returns the found word
             }
         }
     }
     return null; //returns null if there is no word of the specified length
   }






   private boolean dfsFindAnyWordOfLength(int i, int j, char[][] board, DictInterface dict, StringBuilder word, boolean[][] visited, int length) {
     if (word.length() == length) { //condition is checking if the length of the constructed word is the same length as the specified length
         int result = dict.searchPrefix(word); //checks if the constructed word is in the dictionary
         if (result == 2 || result == 3) { //this condition return true if its found in the dictionary
             //System.out.println("Found word: " + word.toString()); //used for debugging reasons
             return true;
         }
         return false;
     }

     if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j]) return false; //this is a base case that checks if the current position is out of bounds or already visited
     visited[i][j] = true; //marks the current position as visited
     word.append(board[i][j]);
     int result = dict.searchPrefix(word); //checks if the current prefix is in the dictionary
     if (result > 0) { //continue if the word prefix is valid
       int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}}; //stores all possible directions
      for (int[] d : directions) { //loops through the board
          if (dfsFindAnyWordOfLength(i + d[0], j + d[1], board, dict, word, visited, length)) { //returns true if a valid word of the specified length is found
              return true;
          }
      }
     }

     word.deleteCharAt(word.length() - 1); //backtracks by removing the last character from the word and remarks the cell as not visited
     visited[i][j] = false;
     return false; //returns false if there isnt a valid word of the specified length
 }




    private String generateRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();

        // System.out.println(generatedString);
        return generatedString;
    }

}
