import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBoard } from '../board.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { BoardService } from '../service/board.service';
import { BoardDeleteDialogComponent } from '../delete/board-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-board',
  templateUrl: './board.component.html',
})
export class BoardComponent implements OnInit {
  boards: IBoard[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected boardService: BoardService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.boards = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.boardService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IBoard[]>) => {
          this.isLoading = false;
          this.paginateBoards(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.boards = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBoard): number {
    return item.id!;
  }

  delete(board: IBoard): void {
    const modalRef = this.modalService.open(BoardDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.board = board;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateBoards(data: IBoard[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.boards.push(d);
      }
    }
  }
}
