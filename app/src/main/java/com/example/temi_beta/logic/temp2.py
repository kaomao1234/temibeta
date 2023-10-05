import heapq

def is_valid(x, y):
    return 0 <= x < 4 and 0 <= y < 4

def manhattan_distance(x1, y1, x2, y2):
    return abs(x1 - x2) + abs(y1 - y2)

def calculate_heuristic(puzzle):
    heuristic = 0
    for i in range(4):
        for j in range(4):
            if puzzle[i][j] != 0:
                target_x = (puzzle[i][j] - 1) // 4
                target_y = (puzzle[i][j] - 1) % 4
                heuristic += manhattan_distance(i, j, target_x, target_y)
    return heuristic

def print_puzzle(puzzle):
    for row in puzzle:
        print(" ".join(map(str, row)))
    print()

def direction(dx, dy):
    if dx == 0 and dy == 1:
        return "Right"
    elif dx == 0 and dy == -1:
        return "Left"
    elif dx == 1 and dy == 0:
        return "Down"
    elif dx == -1 and dy == 0:
        return "Up"

def solve_puzzle(puzzle):
    goal_state = [
        [1, 2, 3, 4],
        [5, 6, 7, 8],
        [9, 10, 11, 12],
        [13, 14, 15, 0]
    ]

    initial_state = (calculate_heuristic(puzzle), 0, puzzle)
    priority_queue = [initial_state]
    visited = set()

    while priority_queue:
        _, moves, current_state = heapq.heappop(priority_queue)
        if current_state == goal_state:
            return moves

        visited.add(tuple(map(tuple, current_state)))
        zero_x, zero_y = None, None

        for i in range(4):
            for j in range(4):
                if current_state[i][j] == 0:
                    zero_x, zero_y = i, j

        for dx, dy in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
            new_x, new_y = zero_x + dx, zero_y + dy
            if is_valid(new_x, new_y):
                new_state = [list(row) for row in current_state]
                new_state[zero_x][zero_y], new_state[new_x][new_y] = new_state[new_x][new_y], new_state[zero_x][zero_y]
                new_heuristic = calculate_heuristic(new_state)
                if tuple(map(tuple, new_state)) not in visited and moves + 1 <= 24:
                    heapq.heappush(priority_queue, (moves + new_heuristic, moves + 1, new_state))
                    print_puzzle(new_state)
                    print(f"Step {moves + 1}: Move {direction(dx, dy)}")

    return -1  # No solution within 24 moves

array_puzzle = [
    [1, 6, 2, 4],
    [9, 5, 3, 8],
    [13, 7, 14, 12],
    [10, 11, 0, 15]
]

steps = solve_puzzle(array_puzzle)
if steps != -1:
    print(f"Solution found in {steps} moves.")
else:
    print("No solution within 24 moves.")
