from pb_py import main as API
import sys
import os

def get_filepaths(directory):
    """
    This function will generate the file names in a directory
    tree by walking the tree either top-down or bottom-up. For each
    directory in the tree rooted at directory top (including top itself),
    it yields a 3-tuple (dirpath, dirnames, filenames).
    """
    file_paths = []  # List which will store all of the full filepaths.
    # Walk the tree.
    for root, directories, files in os.walk(directory):
        for filename in files:
            # Join the two strings in order to form the full filepath.
            filepath = os.path.join(root, filename)
            file_paths.append(filepath)  # Add it to the list.
return file_paths  # Self-explanatory.
# Run the above function and store its results in a variable.
full_file_paths = get_filepaths("/Users/timothysum/dev/pepperSocialRobotics/rosie/lib");

host = "aiaas.pandorabots.com"
input = sys.argv
user_key = "replace"
app_id = "replace"
botname = "peppersocial"

# Bot creation
# result = API.create_bot(user_key, app_id, host, botname)
# print(result)

# Bot listing
result = API.list_bots(user_key, app_id, host)
print result

# Files such as AIML uploading
# for files in full_file_paths:
#     print files
#     result = API.upload_file(user_key, app_id, host, botname, files)
#     print result

# List files
result = API.list_files(user_key, app_id, host, botname)

# Retrieve files
# filename = 'udc.aiml'
# result = API.get_file(user_key, app_id, host, botname, filename)
# print result

# Bot Compilation
result = API.compile_bot(user_key, app_id, host, botname)

# Send input to Bot
result = API.talk(user_key, app_id, host, botname, input)

bot_response = result['response']
session_id = result['sessionid']
print bot_response