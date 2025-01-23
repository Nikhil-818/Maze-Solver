interface MazeSolverMetrics {
    long getSteps();

    long getElapsedTime();

    double getAverageSteps();

    int getNodesExplored();

    double getMemoryUsed();

    int[][] getSolution();
}