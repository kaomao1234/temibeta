from collections import deque

def is_valid(x, y):
    return 0 <= x < 3 and 0 <= y < 3

def swap(arr, x1, y1, x2, y2):
    arr[x1][y1], arr[x2][y2] = arr[x2][y2], arr[x1][y1]

def is_goal(arr):
    return arr == [[1, 2, 3], [4, 5, 6], [7, 8, 0]]

def print_solution(path):
    for step, move in enumerate(path):
        print(f"Step {step + 1}: Move {move}")
        apply_move(array_puzzle, move)
        print_array(array_puzzle)

def print_array(arr):
    for row in arr:
        print(row)
    print()

def get_possible_moves(x, y):
    moves = []
    if is_valid(x - 1, y):
        moves.append('Up')
    if is_valid(x + 1, y):
        moves.append('Down')
    if is_valid(x, y - 1):
        moves.append('Left')
    if is_valid(x, y + 1):
        moves.append('Right')
    return moves

def apply_move(arr, move):
    x, y = find_zero(arr)
    if move == 'Up':
        swap(arr, x, y, x - 1, y)
    elif move == 'Down':
        swap(arr, x, y, x + 1, y)
    elif move == 'Left':
        swap(arr, x, y, x, y - 1)
    elif move == 'Right':
        swap(arr, x, y, x, y + 1)

def find_zero(arr):
    for i in range(3):
        for j in range(3):
            if arr[i][j] == 0:
                return i, j

def solve_puzzle(initial_state):
    queue = deque([(initial_state, [])])
    visited = set()

    while queue:
        current_state, path = queue.popleft()
        if is_goal(current_state):
            return path
        if len(path) >= 20:
            continue

        x, y = find_zero(current_state)
        possible_moves = get_possible_moves(x, y)

        for move in possible_moves:
            new_state = [row[:] for row in current_state]
            apply_move(new_state, move)
            if tuple(map(tuple, new_state)) not in visited:
                visited.add(tuple(map(tuple, new_state)))
                queue.append((new_state, path + [move]))

    return None

array_puzzle = [
    [5, 8, 2],
    [7, 4, 3],
    [1, 6, 0]
]

solution = solve_puzzle(array_puzzle)
if solution:
    print_solution(solution)
else:
    print("No solution found within 20 steps.")
