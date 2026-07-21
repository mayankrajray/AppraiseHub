const steps = [
  { title: "Wait for a cycle", body: "HR opens an appraisal cycle for your department. You'll get a notification when it starts." },
  { title: "Write your self-assessment", body: "Open the appraisal, fill in what went well, what to improve, and your key achievements, then give yourself a rating." },
  { title: "Submit for review", body: "Submitting locks your entry and notifies your manager. You can save a draft any time before submitting." },
  { title: "Manager review", body: "Your manager adds their own notes and rating. HR then approves the completed appraisal." },
  { title: "Acknowledge", body: "Once approved, review the final appraisal and acknowledge it to close out the cycle." },
];

export function HowToUse() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-glow-100">Guide</h1>
        <p className="text-sm text-glow-100/50">How the appraisal cycle works, step by step</p>
      </div>
      <div className="space-y-3">
        {steps.map((s, i) => (
          <div key={s.title} className="panel flex gap-4 p-5">
            <span className="grid h-8 w-8 shrink-0 place-items-center rounded-full bg-glow-500/20 font-display font-bold text-glow-300">
              {i + 1}
            </span>
            <div>
              <p className="font-medium text-glow-100">{s.title}</p>
              <p className="text-sm text-glow-100/60">{s.body}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
