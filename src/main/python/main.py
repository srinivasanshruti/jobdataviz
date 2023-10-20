import requests


search_strings = {"data", "analytics"}
noc_codes = {}
for q in search_strings:
    r = requests.get(
            "https://www.jobbank.gc.ca/core/ta-jobtitle_en/select?q=" + q + "&wt=json&fq=noc_job_title_type_id:1&fl=title,noc_job_title_concordance_id,noc21_code")
    json_response = r.json()
    print(json_response["response"]["docs"])

print("hi")

