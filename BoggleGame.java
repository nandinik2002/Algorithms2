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
      HashSet<String> foundWords = new HashSet<>();
      boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[0].length];

      for (int i = 0; i < boggleBoard.length; i++) {
          for (int j = 0; j < boggleBoard[0].length; j++) {
              dfsCountWords(i, j, boggleBoard, dictionary, new StringBuilder(), visited, foundWords);
          }
      }
      return foundWords.size();
}


private void dfsCountWords(int i, int j, char[][] board, DictInterface dict, StringBuilder word, boolean[][] visited, HashSet<String> foundWords) {
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j]) return;
    word.append(Character.toLowerCase(board[i][j]));
    visited[i][j] = true;

    int result = dict.searchPrefix(new StringBuilder(word));
    if ((result == 2 || result == 3)) {
        foundWords.add(word.toString());
    }
    if (result == 1 || result == 3) {
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di != 0 || dj != 0) {
                    dfsCountWords(i + di, j + dj, board, dict, word, visited, foundWords);
                }
            }
        }
    }

    word.setLength(word.length() - 1);
    visited[i][j] = false;
}






    @Override
    public int countWordsOfCertainLength(char[][] boggleBoard, DictInterface dictionary, int wordLength) {
      int count = 0;
    boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[0].length];
    StringBuilder word = new StringBuilder();

    for (int i = 0; i < boggleBoard.length; i++) {
        for (int j = 0; j < boggleBoard[i].length; j++) {
            count += dfsCountWordsOfCertainLength(i, j, boggleBoard, dictionary, word, visited, 0, wordLength);
        }
    }
    return count;
 }

 private int dfsCountWordsOfCertainLength(int i, int j, char[][] board, DictInterface dict, StringBuilder word, boolean[][] visited, int currentLength, int wordLength) {
    if (currentLength == wordLength) {
        if (dict.searchPrefix(new StringBuilder(word.toString().toLowerCase())) == 2) return 1;
        return 0; // Only count if it's a valid word
    }
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j]) return 0;

    visited[i][j] = true;
    word.append(Character.toLowerCase(board[i][j]));
    int count = 0;

    // Increment currentLength since we've added a new character
    currentLength++;

    // Only continue if currentLength is less than wordLength
    if (currentLength <= wordLength) {
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int[] d : directions) {
            count += dfsCountWordsOfCertainLength(i + d[0], j + d[1], board, dict, word, visited, currentLength, wordLength);
        }
    }

    // Backtrack
    word.setLength(word.length() - 1);
    visited[i][j] = false;
    return count;
}




    @Override
    public boolean isWordInDictionary(DictInterface dictionary, String word) {
      return dictionary.searchPrefix(new StringBuilder(word)) == 2;
    }

    @Override
    public boolean isWordInBoard(char[][] boggleBoard, String word) {
      word = word.toUpperCase();
      for(int i = 0; i < boggleBoard.length; i++) {
          for(int j = 0; j < boggleBoard[0].length; j++) {
              if(checkWordHelper(boggleBoard, i, j, word, 0))
                  return true;
          }
      }
      return false;
    }

    private boolean checkWordHelper(char[][] boggleBoard, int row, int col, String word, int index) {
        if(index == word.length())
            return true;
        if(row < 0 || col < 0 || row >= boggleBoard.length || col >= boggleBoard[0].length || boggleBoard[row][col] != word.charAt(index))
            return false;

        char temp = boggleBoard[row][col];
        boggleBoard[row][col] = '#'; // Marking visited

        boolean found = checkWordHelper(boggleBoard, row + 1, col, word, index + 1) ||
                        checkWordHelper(boggleBoard, row - 1, col, word, index + 1) ||
                        checkWordHelper(boggleBoard, row, col + 1, word, index + 1) ||
                        checkWordHelper(boggleBoard, row, col - 1, word, index + 1) ||
                        checkWordHelper(boggleBoard, row + 1, col + 1, word, index + 1) ||
                        checkWordHelper(boggleBoard, row + 1, col - 1, word, index + 1) ||
                        checkWordHelper(boggleBoard, row - 1, col + 1, word, index + 1) ||
                        checkWordHelper(boggleBoard, row - 1, col - 1, word, index + 1);

        boggleBoard[row][col] = temp; // Restoring the original character

        return found;
    }




    @Override
    public String anyWord(char[][] boggleBoard, DictInterface dictionary) {
      boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[0].length];
     StringBuilder word = new StringBuilder();
     for (int i = 0; i < boggleBoard.length; i++) {
         for (int j = 0; j < boggleBoard[0].length; j++) {
             if (dfsFindAnyWord(i, j, boggleBoard, dictionary, word, visited)) {
                 return word.toString();
             }
         }
     }
     return null;
  }

  private boolean dfsFindAnyWord(int i, int j, char[][] board, DictInterface dict, StringBuilder word, boolean[][] visited) {
    if (dict.searchPrefix(word) == 2 || dict.searchPrefix(word) == 3) {
        return true;
    }

    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j]) return false;

    visited[i][j] = true;
    word.append(board[i][j]);

    // Explore all 8 directions
    int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
    for (int[] d : directions) {
        int newRow = i + d[0];
        int newCol = j + d[1];
        if (dfsFindAnyWord(newRow, newCol, board, dict, word, visited)) {
            return true;
        }
    }

    word.deleteCharAt(word.length() - 1); // Backtrack
    visited[i][j] = false;
    return false;
}



    @Override
    public ArrayList<Tile> markWordInBoard(char[][] boggleBoard, String word) {
      if (word == null || word.isEmpty()) {
          //System.out.println("No word provided or word is empty.");
          return new ArrayList<>(); // Return an empty list to indicate no operation was performed.
      }

      for (int i = 0; i < boggleBoard.length; i++) {
          for (int j = 0; j < boggleBoard[i].length; j++) {
              // Start DFS if the first character matches
              if (boggleBoard[i][j] == word.charAt(0)) {
                  ArrayList<Tile> path = new ArrayList<>();
                  boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[0].length];
                  if (dfsMarkWordInBoard(i, j, boggleBoard, word, 0, visited, path)) {
                      return path; // Return the path if the word is found
                  }
              }
          }
      }
      return new ArrayList<>();
  }



    private boolean dfsMarkWordInBoard(int i, int j, char[][] board, String word, int index, boolean[][] visited, ArrayList<Tile> path) {
    if (index == word.length()) {
        return true; // Entire word has been found
    }
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
        return false; // Out of bounds
    }
    if (visited[i][j] || board[i][j] != word.charAt(index)) {
        return false; // Either visited or not matching the current character in the word
    }

    visited[i][j] = true; // Mark as visited
    path.add(new Tile(i, j)); // Add current tile to the path

    // Explore all 8 directions
    int[][] dirs = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
    for (int[] d : dirs) {
        if (dfsMarkWordInBoard(i + d[0], j + d[1], board, word, index + 1, visited, path)) {
            return true; // Continue if next character can be found
        }
    }

    // Backtrack if not found
    path.remove(path.size() - 1); // Remove the last added tile
    visited[i][j] = false; // Unmark this cell as visited
    return false;
}

    @Override
    public boolean checkTiles(char[][] boggleBoard, ArrayList<Tile> tiles, String word) {
      if (tiles.size() != word.length()) return false;

      // Check each tile in the list to see if it matches the corresponding character in the word
      for (int i = 0; i < tiles.size(); i++) {
          Tile tile = tiles.get(i);
          if (boggleBoard[tile.row][tile.col] != word.charAt(i)) {
              return false; // Tile does not match the letter in the word
          }

          // If not the first tile, check if it's adjacent to the previous tile
          if (i > 0 && !isAdjacent(tiles.get(i - 1), tile)) {
              return false; // Tiles are not adjacent
          }
      }
      return true;
    }

    private boolean isAdjacent(Tile prevTile, Tile currTile) {
    // Calculate the differences in row and column indices
    int rowDiff = Math.abs(prevTile.row - currTile.row);
    int colDiff = Math.abs(prevTile.col - currTile.col);

    // Tiles are adjacent if neither difference is greater than 1
    // This allows for horizontal, vertical, and diagonal adjacency
    return rowDiff <= 1 && colDiff <= 1 && !(rowDiff == 0 && colDiff == 0); // Ensure it's not the same tile
}



    @Override
    public String anyWord(char[][] boggleBoard, DictInterface dictionary, int length) {
      boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[0].length];

     for (int i = 0; i < boggleBoard.length; i++) {
         for (int j = 0; j < boggleBoard[i].length; j++) {
             StringBuilder word = new StringBuilder(); // Initialize a new StringBuilder for each starting point
             if (dfsFindAnyWordOfLength(i, j, boggleBoard, dictionary, word, visited, length)) {
                 return word.toString();
             }
         }
     }
     return null;
   }


   private boolean dfsFindAnyWordOfLength(int i, int j, char[][] board, DictInterface dict, StringBuilder word, boolean[][] visited, int length) {
     if (word.length() == length) {
         int result = dict.searchPrefix(word);
         if (result == 2 || result == 3) {
             System.out.println("Found word: " + word.toString()); // Print the found word
             return true;
         }
         return false;
     }

     if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j]) return false;

     visited[i][j] = true;
     word.append(board[i][j]);

     int result = dict.searchPrefix(word);

     if (result > 0) { // Continue searching if it's a prefix
         for (int di = -1; di <= 1; di++) {
             for (int dj = -1; dj <= 1; dj++) {
                 if (di != 0 || dj != 0) {
                     if (dfsFindAnyWordOfLength(i + di, j + dj, board, dict, word, visited, length)) {
                         return true;
                     }
                 }
             }
         }
     }

     word.deleteCharAt(word.length() - 1); // Backtrack
     visited[i][j] = false;
     return false;
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
