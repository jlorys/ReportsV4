import {Component} from '@angular/core';
import {MdDialogRef} from '@angular/material';

@Component({
  selector: 'app-confirm-delete-dialog',
  template: `
    <h2 md-dialog-title>Potwierdzenie usunięcia</h2>
    <md-dialog-content>
      Czy na pewno chcesz usunąć ten rekord?
    </md-dialog-content>

    <md-dialog-actions>
      <button md-raised-button (click)="dialogRef.close('cancel')">Nie</button>&nbsp;
      <button md-raised-button (click)="dialogRef.close('delete')">Tak</button>
    </md-dialog-actions>
  `
})
export class ConfirmDeleteDialogComponent {
  constructor(public dialogRef: MdDialogRef<ConfirmDeleteDialogComponent>) {}
}
