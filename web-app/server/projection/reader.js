const toRecord = row => ({
  id: row.id,
  customerName: row.customer_name,
  customerEmail: row.customer_email,
  issueDate: row.issue_date,
  dueDate: row.due_date,
  total: row.total,
  status: row.status
});

export const reader = conn => () => new Promise((resolve, reject) => {
  conn.query('SELECT * FROM invoices', (error, results) => {
    if (error) {
      reject(error);
    } else {
      resolve(results.map(toRecord));
    }
  })
});
