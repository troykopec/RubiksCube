
Solving a Rubiks Cube Using Graph Theory

Can be ran in the main.java file at
src/test/java/ShortestPathTESTING


In this innovative project, I have developed a unique code-based approach to solving a Rubik's Cube by leveraging the principles of graph theory. The core of this method involves the simultaneous expansion of two distinct trees: one originating from a solved state of the cube and the other from a randomly scrambled cube.

One mechanism of this algorithm is an efficient use of the Breadth-First Search (BFS) technique. As the front tree (representing the scrambled state) expands, BFS is diligently applied to systematically explore and evaluate each level of the tree. This process is crucial for identifying potential solutions during the tree's creation.

Simultaneously we recognize that some cube states might not be easily reachable through this method, so a Depth-First Search (DFS) was used to traverse the back tree, which expands from the solved cube state.
To efficiently navigate through the vast possibilities of the Rubik's Cube, I employed multi-threading for the creation of the two trees, creating them both simultaneously. Multi-threading is also used during the DFS phase of the algorithm. These additions significantly enhances the search speed by 100%, making the solution-finding process both faster and more robust.

Upon discovering a viable solution path, the algorithm doesn't stop at identification. Instead, it traces back the path leading to the solution. This retracing provides valuable insights into the steps required to solve the Rubik's Cube from any given scrambled state. The final stage of the algorithm is a comprehensive output, where the solution path is clearly printed.

This project employs graph theory to a real-world problem but also demonstrates the power of combining different search strategies and multi-threading in algorithm design. It's an excellent example of how computational methods can be employed to tackle complex, seemingly intractable puzzles in an efficient an manner.
